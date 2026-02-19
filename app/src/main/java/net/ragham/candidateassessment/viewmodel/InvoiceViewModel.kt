package net.ragham.candidateassessment.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import net.ragham.candidateassessment.domain.InvoiceCalculator
import net.ragham.candidateassessment.domain.InvoiceItem
import net.ragham.candidateassessment.domain.Product
import java.math.BigDecimal

//  ویومدل برای مدیریت وضعیت فاکتور
class InvoiceViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(InvoiceUiState())
    val uiState: StateFlow<InvoiceUiState> = _uiState.asStateFlow()

    // لیست محصولات با قیمت تومان
    private val _products = listOf(
        Product("1", "لپ تاب", BigDecimal("50000000")),
        Product("2", "موس", BigDecimal("500000")),
        Product("3", "صفحه ملید", BigDecimal("1200000")),
        Product("4", "مانیتور", BigDecimal("8000000")),
        Product("5", "کابل HDMI", BigDecimal("250000"))
    )
    val products = _products

    // بروز کردن اطلاعات مشتری
    fun updateCustomerInfo(name: String, phone: String) {
        _uiState.update { it.copy(customerName = name, customerPhone = phone) }
    }

    // اافه کردن یک آیتم به فاکتور
    fun addOrUpdateItem(item: InvoiceItem) {
        _uiState.update { currentState ->
            val updatedItems = currentState.items.toMutableList()
            val existingIndex = updatedItems.indexOfFirst { it.id == item.id }

            if (existingIndex != -1) {
                updatedItems[existingIndex] = item
            } else {
                updatedItems.add(item)
            }
            
            recalculateTotals(currentState.copy(items = updatedItems))
        }
    }

    // پاک کردن یک آیتم از لیست
    fun removeItem(item: InvoiceItem) {
        _uiState.update { currentState ->
            val updatedItems = currentState.items.filter { it.id != item.id }
            recalculateTotals(currentState.copy(items = updatedItems))
        }
    }

    // گرفتن آیتم با آی دی
    fun getItem(itemId: String): InvoiceItem? {
        return _uiState.value.items.find { it.id == itemId }
    }


    fun resetInvoice() {
        _uiState.value = InvoiceUiState()
    }

    // محاسبه کل فاکتور
    private fun recalculateTotals(state: InvoiceUiState): InvoiceUiState {
        return state.copy(
            totalBeforeDiscount = InvoiceCalculator.calculateTotalBeforeDiscount(state.items),
            totalDiscount = InvoiceCalculator.calculateTotalDiscount(state.items),
            finalTotal = InvoiceCalculator.calculateFinalTotal(state.items)
        )
    }
}
