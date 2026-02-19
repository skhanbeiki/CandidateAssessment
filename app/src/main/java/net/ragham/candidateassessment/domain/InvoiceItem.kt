package net.ragham.candidateassessment.domain

import java.math.BigDecimal
import java.util.UUID

/**
 * این یک سطر از فاکتوره که توی صفحه بعدی میسازیم
 *
 * id
 * product محصول انتخاب شده
 * quantity تعداد محصول
 * unitPrice قیمت محصول - ممکنه پیش فرض هم داشته باشه
 * discountAmount مبلغ تخفیف (تومان)
 * discountPercent درصد تخفیف
 */

data class InvoiceItem(
    val id: String = UUID.randomUUID().toString(),
    val product: Product,
    val quantity: BigDecimal = BigDecimal.ONE,
    val unitPrice: BigDecimal = product.defaultPrice,
    val discountAmount: BigDecimal = BigDecimal.ZERO,
    val discountPercent: BigDecimal = BigDecimal.ZERO
)
