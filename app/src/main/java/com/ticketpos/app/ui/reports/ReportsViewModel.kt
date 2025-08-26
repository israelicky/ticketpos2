package com.ticketpos.app.ui.reports

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ticketpos.app.data.repository.ProductRepository
import com.ticketpos.app.data.repository.SaleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReportsViewModel @Inject constructor(
    private val saleRepository: SaleRepository,
    private val productRepository: ProductRepository
) : ViewModel() {

    private val _todaySales = MutableLiveData<Double>()
    val todaySales: LiveData<Double> = _todaySales

    private val _monthSales = MutableLiveData<Double>()
    val monthSales: LiveData<Double> = _monthSales

    private val _totalProducts = MutableLiveData<Int>()
    val totalProducts: LiveData<Int> = _totalProducts

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun loadReportData() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                
                // Load today's sales
                val todayTotal = saleRepository.getTodayTotalSales()
                _todaySales.value = todayTotal
                
                // Load month sales (simplified - using today's data)
                _monthSales.value = todayTotal * 30 // Mock data
                
                // Load product count
                productRepository.getAllProducts().collect { products ->
                    _totalProducts.value = products.size
                }
                
            } catch (e: Exception) {
                _todaySales.value = 0.0
                _monthSales.value = 0.0
                _totalProducts.value = 0
            } finally {
                _isLoading.value = false
            }
        }
    }
}
