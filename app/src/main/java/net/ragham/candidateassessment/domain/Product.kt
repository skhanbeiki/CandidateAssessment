package net.ragham.candidateassessment.domain

import java.math.BigDecimal

//   این مدل برای محصوله که اطلاعات اونو داخلش ذخیره میکنیم

data class Product(
    val id: String,
    val name: String,
    val defaultPrice: BigDecimal
)
