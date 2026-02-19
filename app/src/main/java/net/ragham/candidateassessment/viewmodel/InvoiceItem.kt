package net.ragham.candidateassessment.viewmodel

import java.math.BigDecimal
import java.util.UUID

data class InvoiceItem(
    val id : String = UUID.randomUUID().toString(),
    val productName : String,
    val amount : Double,
    val unitPrice : Double,
    val discountAmount : Double,
    val discountPrecent : Double
)
