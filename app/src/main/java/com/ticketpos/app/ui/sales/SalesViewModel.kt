package com.ticketpos.app.ui.sales

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ticketpos.app.data.entity.Sale
import com.ticketpos.app.data.repository.SaleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SalesViewModel @Inject constructor(
    private val saleRepository: SaleRepository
) : ViewModel() {

    private val _sales = MutableLiveData<List<Sale>>()
    val sales: LiveData<List<Sale>> = _sales

    private val _todayTotal = MutableLiveData<Double>()
    val todayTotal: LiveData<Double> = _todayTotal

    private val _todaySalesCount = MutableLiveData<Int>()
    val todaySalesCount: LiveData<Int> = _todaySalesCount

    private val _selectedSale = MutableLiveData<Sale?>()
    val selectedSale: LiveData<Sale?> = _selectedSale

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun loadTodaySales() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                
                // Load today's sales
                saleRepository.getTodaySales().collect { salesList ->
                    _sales.value = salesList
                }
                
                // Load today's totals
                val todayTotal = saleRepository.getTodayTotalSales()
                _todayTotal.value = todayTotal
                
                val todayCount = saleRepository.getTodaySalesCount()
                _todaySalesCount.value = todayCount
                
            } catch (e: Exception) {
                // Handle error
                _sales.value = emptyList()
                _todayTotal.value = 0.0
                _todaySalesCount.value = 0
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun selectSale(sale: Sale) {
        _selectedSale.value = sale
    }

    fun clearSelection() {
        _selectedSale.value = null
    }
}
