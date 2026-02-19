package net.ragham.candidateassessment.ui.util

import java.math.BigDecimal
import java.text.DecimalFormat

object NumberUtils {
    private val currencyFormat = DecimalFormat("#,###")
    private val decimalFormat = DecimalFormat("0.###")

    //جدا کردن مبلغ 3 رقمی به تومان
    fun formatCurrency(amount: BigDecimal): String {
        return currencyFormat.format(amount.setScale(0, java.math.RoundingMode.HALF_UP))
    }

    fun formatCurrencyInput(input: String): String {
        if (input.isEmpty()) return ""
        val raw = input.replace(",", "")
        if (raw.isEmpty()) return ""
        return try {
            val number = BigDecimal(raw)
            currencyFormat.format(number)
        } catch (e: Exception) {
            input
        }
    }

   //حذف کردن کاما
    fun parseCurrencyInput(input: String): BigDecimal {
        if (input.isEmpty()) return BigDecimal.ZERO
        val raw = input.replace(",", "")
        return try {
            BigDecimal(raw)
        } catch (e: Exception) {
            BigDecimal.ZERO
        }
    }

    //فرمت کردن مقدار اعشاری
    fun formatDecimal(value: BigDecimal): String {
        return decimalFormat.format(value)
    }

    //بررسی معتبر بودن عدد صحیح
    fun isValidIntegerInput(input: String): Boolean {
        if (input.isEmpty()) return true
        val raw = input.replace(",", "")
        return raw.matches(Regex("^\\d+$"))
    }
}
