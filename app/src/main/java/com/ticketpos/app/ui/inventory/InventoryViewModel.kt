package com.ticketpos.app.ui.inventory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ticketpos.app.data.entity.Product
import com.ticketpos.app.data.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InventoryViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : ViewModel() {

    private val _products = MutableLiveData<List<Product>>()
    val products: LiveData<List<Product>> = _products

    private val _selectedProduct = MutableLiveData<Product?>()
    val selectedProduct: LiveData<Product?> = _selectedProduct

    private val _lowStockCount = MutableLiveData<Int>()
    val lowStockCount: LiveData<Int> = _lowStockCount

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun loadProducts() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                
                productRepository.getAllProducts().collect { productList ->
                    _products.value = productList
                    _lowStockCount.value = productList.count { it.stock <= 10 }
                }
                
            } catch (e: Exception) {
                _products.value = emptyList()
                _lowStockCount.value = 0
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun selectProduct(product: Product) {
        _selectedProduct.value = product
    }

    fun clearSelection() {
        _selectedProduct.value = null
    }
}
