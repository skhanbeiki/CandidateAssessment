package net.ragham.candidateassessment.domain

import java.math.BigDecimal
import java.math.RoundingMode


object InvoiceCalculator {

    // دقت محاسبات (۳ رقم اعشار)
    private const val SCALE = 3
    //  گرد کردن (نصف به سمت بالا)
    private val ROUNDING_MODE = RoundingMode.HALF_UP

    /**
     * محاسبه جمع جزئی یک سطر
     */
    fun calculateRowSubtotal(quantity: BigDecimal, unitPrice: BigDecimal): BigDecimal {
        return quantity.multiply(unitPrice).setScale(SCALE, ROUNDING_MODE)
    }

    /**
     *محسبه تخفیف طبق داکیومنت
     */
    fun calculateEffectiveDiscount(
        subtotal: BigDecimal,
        discountAmount: BigDecimal,
        discountPercent: BigDecimal
    ): BigDecimal {
        val calculatedDiscount = if (discountAmount > BigDecimal.ZERO) {
            discountAmount
        } else if (discountPercent > BigDecimal.ZERO) {
            subtotal.multiply(discountPercent)
                .divide(BigDecimal(100), SCALE, ROUNDING_MODE)
        } else {
            BigDecimal.ZERO
        }

        // اطمینان از اینکه تخفیف از مبلغ کل بیشتر نباشد
        return calculatedDiscount.min(subtotal).setScale(SCALE, ROUNDING_MODE)
    }

    /**
     * محاسبه مبلغ قابل پرداخت نهایی یک سطر.
     */
    fun calculateRowTotal(subtotal: BigDecimal, effectiveDiscount: BigDecimal): BigDecimal {
        return subtotal.subtract(effectiveDiscount).max(BigDecimal.ZERO).setScale(SCALE, ROUNDING_MODE)
    }

    /**
     * جمع کل مبالغ قبل از تخفیف برای کل فاکتور.
     */
    fun calculateTotalBeforeDiscount(items: List<InvoiceItem>): BigDecimal {
        return items.fold(BigDecimal.ZERO) { acc, item ->
            acc.add(calculateRowSubtotal(item.quantity, item.unitPrice))
        }.setScale(SCALE, ROUNDING_MODE)
    }




    /**
     * جمع کل تخفیفات برای کل فاکتور.
     */
    fun calculateTotalDiscount(items: List<InvoiceItem>): BigDecimal {
        return items.fold(BigDecimal.ZERO) { acc, item ->
            val subtotal = calculateRowSubtotal(item.quantity, item.unitPrice)
            val discount = calculateEffectiveDiscount(subtotal, item.discountAmount, item.discountPercent)
            acc.add(discount)
        }.setScale(SCALE, ROUNDING_MODE)
    }



    /**
     * مبلغ قابل پرداخت نهایی کل فاکتور.
     */
    fun calculateFinalTotal(items: List<InvoiceItem>): BigDecimal {
        return items.fold(BigDecimal.ZERO) { acc, item ->
            val subtotal = calculateRowSubtotal(item.quantity, item.unitPrice)
            val effectiveDiscount = calculateEffectiveDiscount(subtotal, item.discountAmount, item.discountPercent)
            val total = calculateRowTotal(subtotal, effectiveDiscount)
            acc.add(total)
        }.setScale(SCALE, ROUNDING_MODE)
    }
}
