package net.ragham.candidateassessment.ui.invoice

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import net.ragham.candidateassessment.domain.InvoiceCalculator
import net.ragham.candidateassessment.domain.InvoiceItem
import net.ragham.candidateassessment.ui.util.NumberUtils
import java.math.BigDecimal

// کامپوننت نمایش یک سطر از فاکتور.
@Composable
fun InvoiceItemRow(
    item: InvoiceItem,
    onDelete: () -> Unit,
    onEdit: () -> Unit
) {
    val subtotal = InvoiceCalculator.calculateRowSubtotal(item.quantity, item.unitPrice)
    val effectiveDiscount = InvoiceCalculator.calculateEffectiveDiscount(
        subtotal,
        item.discountAmount,
        item.discountPercent
    )
    val total = InvoiceCalculator.calculateRowTotal(subtotal, effectiveDiscount)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp)
            .clickable { onEdit() },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.product.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "تعداد: ${item.quantity} | قیمت واحد: ${NumberUtils.formatCurrency(item.unitPrice)}")
                if (effectiveDiscount > BigDecimal.ZERO) {
                    Text(
                        text = "تخفیف: ${NumberUtils.formatCurrency(effectiveDiscount)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error
                    )
                }
                Text(
                    text = "جمع: ${NumberUtils.formatCurrency(total)}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}
