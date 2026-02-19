package net.ragham.candidateassessment.viewmodel

data class InvoiceUiState(
    val customerName : String = "",
    val customerPhone: String = "",
    val items: List<InvoiceItem> = emptyList() ,

)
