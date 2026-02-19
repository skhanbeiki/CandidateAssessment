package net.ragham.candidateassessment.viewmodel

import net.ragham.candidateassessment.domain.InvoiceItem
import java.math.BigDecimal

/**
 * وضعیت رابط کاربری فاکتور.
 * Represents the UI state of the invoice.
 *
 * @property items لیست آیتم‌های فاکتور
 * @property customerName نام مشتری
 * @property customerPhone شماره تماس مشتری
 * @property totalBeforeDiscount جمع کل قبل از تخفیف
 * @property totalDiscount جمع کل تخفیف
 * @property finalTotal مبلغ قابل پرداخت نهایی
 */
data class InvoiceUiState(
    val items: List<InvoiceItem> = emptyList(),
    val customerName: String = "",
    val customerPhone: String = "",
    val totalBeforeDiscount: BigDecimal = BigDecimal.ZERO,
    val totalDiscount: BigDecimal = BigDecimal.ZERO,
    val finalTotal: BigDecimal = BigDecimal.ZERO
)
