package net.ragham.candidateassessment.ui.invoice

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import net.ragham.candidateassessment.domain.InvoiceItem
import net.ragham.candidateassessment.domain.Product
import net.ragham.candidateassessment.ui.util.NumberUtils
import java.math.BigDecimal
import java.math.RoundingMode

//صفحه افزودن یا ویرایش آیتم فاکتور.

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InvoiceItemAddScreen(
    products: List<Product>,
    initialItem: InvoiceItem?,
    onSave: (InvoiceItem) -> Unit,
    onCancel: () -> Unit
) {
    var selectedProduct by remember { mutableStateOf(initialItem?.product ?: products.firstOrNull()) }
    var quantityStr by remember { mutableStateOf(initialItem?.quantity?.toPlainString() ?: "") }
    
    // قیمت واحد
    var unitPriceStr by remember { 
        mutableStateOf(NumberUtils.formatCurrencyInput(initialItem?.unitPrice?.toPlainString() ?: initialItem?.product?.defaultPrice?.toPlainString() ?: "")) 
    }
    
    // مبلغ تخفیف
    var discountAmountStr by remember { 
        mutableStateOf(NumberUtils.formatCurrencyInput(initialItem?.discountAmount?.takeIf { it > BigDecimal.ZERO }?.toPlainString() ?: "")) 
    }
    
    var discountPercentStr by remember { mutableStateOf(initialItem?.discountPercent?.takeIf { it > BigDecimal.ZERO }?.toPlainString() ?: "") }
    var expanded by remember { mutableStateOf(false) }

    // محاسبه مقادیر لحظه‌ای در سامری و منطق تخفیف
    val quantity = quantityStr.toBigDecimalOrNull() ?: BigDecimal.ZERO
    
    // پارس کردن قیمت واحد (حذف کاما)
    val unitPrice = NumberUtils.parseCurrencyInput(unitPriceStr)
    
    val subtotal = quantity.multiply(unitPrice).setScale(3, RoundingMode.HALF_UP)
    
    // محاسبه تخفیف و مبلغ نهایی برای نمایش (بدون تغییر استیت ورودی‌ها در اینجا)
    // پارس کردن مبلغ تخفیف (حذف کاما)
    val currentDiscountAmount = NumberUtils.parseCurrencyInput(discountAmountStr)
    val currentDiscountPercent = discountPercentStr.toBigDecimalOrNull() ?: BigDecimal.ZERO
    
    // اگر مبلغ وارد شده همان است. اگر نه و درصد وارد شده، محاسبه می‌شود.
    val effectiveDiscountAmount = if (currentDiscountAmount > BigDecimal.ZERO) {
        currentDiscountAmount
    } else if (currentDiscountPercent > BigDecimal.ZERO) {
        subtotal.multiply(currentDiscountPercent).divide(BigDecimal(100), 3, RoundingMode.HALF_UP)
    } else {
        BigDecimal.ZERO
    }.min(subtotal)

    val finalTotal = subtotal.subtract(effectiveDiscountAmount).max(BigDecimal.ZERO)


    // به‌روزرسانی قیمت واحد هنگام تغییر محصول
    LaunchedEffect(selectedProduct) {
        if (initialItem == null && selectedProduct != null) {
             if (unitPriceStr.isEmpty()) {
                 unitPriceStr = NumberUtils.formatCurrency(selectedProduct!!.defaultPrice)
             }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (initialItem == null) "افزودن آیتم" else "ویرایش آیتم") },
                navigationIcon = {
                    IconButton(onClick = onCancel) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = selectedProduct?.name ?: "انتخاب محصول",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("محصول") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    products.forEach { product ->
                        DropdownMenuItem(
                            text = { Text(product.name) },
                            onClick = {
                                selectedProduct = product
                                unitPriceStr = NumberUtils.formatCurrency(product.defaultPrice)
                                expanded = false
                            }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = quantityStr,
                onValueChange = { if (isValidDecimal(it)) quantityStr = it },
                label = { Text("تعداد") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = unitPriceStr,
                onValueChange = { input ->
                    if (NumberUtils.isValidIntegerInput(input)) {
                        unitPriceStr = NumberUtils.formatCurrencyInput(input)
                    }
                },
                label = { Text("قیمت واحد (تومان)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = discountAmountStr,
                onValueChange = { input ->
                    if (NumberUtils.isValidIntegerInput(input)) {
                        val formattedInput = NumberUtils.formatCurrencyInput(input)
                        discountAmountStr = formattedInput

                        val amount = NumberUtils.parseCurrencyInput(formattedInput)
                        
                        if (subtotal > BigDecimal.ZERO) {
                            val percent = amount.multiply(BigDecimal(100)).divide(subtotal, 3, RoundingMode.HALF_UP)
                            discountPercentStr = percent.stripTrailingZeros().toPlainString()
                        }
                    }
                },
                label = { Text("مبلغ تخفیف (تومان)") },
                isError = NumberUtils.parseCurrencyInput(discountAmountStr) > subtotal,
                supportingText = {
                    val currentAmount = NumberUtils.parseCurrencyInput(discountAmountStr)
                    if (currentAmount > subtotal) {
                        Text("مبلغ تخفیف نمی‌تواند از قیمت کل (${NumberUtils.formatCurrency(subtotal)}) بیشتر باشد")
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            // Discount Percent
            OutlinedTextField(
                value = discountPercentStr,
                onValueChange = { input ->
                    if (isValidDecimal(input)) {
                        discountPercentStr = input
                        // محاسبه مبلغ تخفیف بر اساس درصد وارد شده
                        val percent = input.toBigDecimalOrNull() ?: BigDecimal.ZERO
                        if (subtotal > BigDecimal.ZERO) {
                            val amount = subtotal.multiply(percent).divide(BigDecimal(100), 3, RoundingMode.HALF_UP)
                            val clampedAmount = amount.min(subtotal)
                            discountAmountStr = NumberUtils.formatCurrency(clampedAmount)
                        }
                    }
                },
                label = { Text("درصد تخفیف") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth()
            )

            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "خلاصه آیتم",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("جمع کل (بدون تخفیف):")
                        Text(NumberUtils.formatCurrency(subtotal))
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("تخفیف:")
                        Text(NumberUtils.formatCurrency(effectiveDiscountAmount))
                    }
                    HorizontalDivider(color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.2f))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "مبلغ نهایی:",
                            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                        )
                        Text(
                            NumberUtils.formatCurrency(finalTotal),
                            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                        )
                    }
                }
            }

            Button(
                onClick = {
                    val discountAmount = NumberUtils.parseCurrencyInput(discountAmountStr)
                    val discountPercent = discountPercentStr.toBigDecimalOrNull() ?: BigDecimal.ZERO
                    
                    // بررسی نهایی برای جلوگیری از ثبت تخفیف غیرمجاز
                    if (discountAmount <= subtotal) {
                         if (selectedProduct != null && quantity > BigDecimal.ZERO) {
                            val newItem = (initialItem ?: InvoiceItem(
                                product = selectedProduct!!,
                                quantity = quantity,
                                unitPrice = unitPrice
                            )).copy(
                                product = selectedProduct!!,
                                quantity = quantity,
                                unitPrice = unitPrice,
                                discountAmount = discountAmount,
                                discountPercent = discountPercent
                            )
                            onSave(newItem)
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = selectedProduct != null && 
                          quantityStr.isNotEmpty() && 
                          NumberUtils.parseCurrencyInput(discountAmountStr) <= subtotal
            ) {
                Text(if (initialItem == null) "افزودن به فاکتور" else "ذخیره تغییرات")
            }
        }
    }
}

// اعتبارسنجی ورودی اعشاری (تا ۳ رقم اعشار)
fun isValidDecimal(input: String): Boolean {
    if (input.isEmpty()) return true
    return input.matches(Regex("^\\d*\\.?\\d{0,3}$"))
}
