package net.ragham.candidateassessment.viewmodel

import net.ragham.candidateassessment.domain.InvoiceItem
import java.math.BigDecimal

/**
 * وضعیت رابط کاربری فاکتور.
 *
 *  items لیست آیتم‌های فاکتور
 *  customerName نام مشتری
 *  customerPhone شماره تماس مشتری
 *  totalBeforeDiscount جمع کل قبل از تخفیف
 *  totalDiscount جمع کل تخفیف
 *  finalTotal مبلغ قابل پرداخت نهایی
 */
data class InvoiceUiState(
    val items: List<InvoiceItem> = emptyList(),
    val customerName: String = "",
    val customerPhone: String = "",
    val totalBeforeDiscount: BigDecimal = BigDecimal.ZERO,
    val totalDiscount: BigDecimal = BigDecimal.ZERO,
    val finalTotal: BigDecimal = BigDecimal.ZERO
)
