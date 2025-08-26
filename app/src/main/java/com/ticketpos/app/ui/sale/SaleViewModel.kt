package com.ticketpos.app.ui.sale

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ticketpos.app.data.entity.Product
import com.ticketpos.app.data.entity.Sale
import com.ticketpos.app.data.entity.SaleItem
import com.ticketpos.app.data.repository.ProductRepository
import com.ticketpos.app.data.repository.SaleRepository
import com.ticketpos.app.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*
import javax.inject.Inject

@HiltViewModel
class SaleViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val saleRepository: SaleRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _cartItems = MutableLiveData<List<CartItem>>()
    val cartItems: LiveData<List<CartItem>> = _cartItems

    private val _searchResults = MutableLiveData<List<Product>>()
    val searchResults: LiveData<List<Product>> = _searchResults

    private val _cartSubtotal = MutableLiveData<BigDecimal>(BigDecimal.ZERO)
    val cartSubtotal: LiveData<BigDecimal> = _cartSubtotal

    private val _cartTax = MutableLiveData<BigDecimal>(BigDecimal.ZERO)
    val cartTax: LiveData<BigDecimal> = _cartTax

    private val _cartDiscount = MutableLiveData<BigDecimal>(BigDecimal.ZERO)
    val cartDiscount: LiveData<BigDecimal> = _cartDiscount

    private val _cartTotal = MutableLiveData<BigDecimal>(BigDecimal.ZERO)
    val cartTotal: LiveData<BigDecimal> = _cartTotal

    private val _currentSaleId = MutableLiveData<Long>()
    val currentSaleId: Long get() = _currentSaleId.value ?: 0

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private var discountPercentage: BigDecimal = BigDecimal.ZERO
    private var currentUser: User? = null

    init {
        loadCurrentUser()
        initializeNewSale()
    }

    private fun loadCurrentUser() {
        viewModelScope.launch {
            try {
                // Load current logged in user
                currentUser = userRepository.getCurrentUser()
            } catch (e: Exception) {
                _errorMessage.value = "Error al cargar usuario: ${e.message}"
            }
        }
    }

    private fun initializeNewSale() {
        _cartItems.value = emptyList()
        _cartSubtotal.value = BigDecimal.ZERO
        _cartTax.value = BigDecimal.ZERO
        _cartDiscount.value = BigDecimal.ZERO
        _cartTotal.value = BigDecimal.ZERO
        discountPercentage = BigDecimal.ZERO
    }

    fun searchProducts(query: String) {
        if (query.isBlank()) return

        viewModelScope.launch {
            try {
                _isLoading.value = true
                val products = productRepository.searchProducts(query)
                _searchResults.value = products
            } catch (e: Exception) {
                _errorMessage.value = "Error en búsqueda: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun addProductToCart(product: Product, quantity: Double) {
        val currentItems = _cartItems.value?.toMutableList() ?: mutableListOf()
        
        // Check if product already exists in cart
        val existingItemIndex = currentItems.indexOfFirst { it.productId == product.id }
        
        if (existingItemIndex != -1) {
            // Update existing item quantity
            val existingItem = currentItems[existingItemIndex]
            val newQuantity = existingItem.quantity + quantity
            currentItems[existingItemIndex] = existingItem.copy(quantity = newQuantity)
        } else {
            // Add new item to cart
            val cartItem = CartItem(
                id = System.currentTimeMillis(), // Temporary ID
                productId = product.id,
                productName = product.name,
                productSku = product.sku,
                productBarcode = product.barcode,
                quantity = quantity,
                unit = product.unit,
                unitPrice = product.price,
                totalPrice = product.price.multiply(BigDecimal.valueOf(quantity)),
                taxRate = product.taxRate,
                costPrice = product.costPrice
            )
            currentItems.add(cartItem)
        }
        
        _cartItems.value = currentItems
        calculateCartTotals()
    }

    fun updateItemQuantity(itemId: Long, newQuantity: Double) {
        if (newQuantity <= 0) {
            removeItemFromCart(itemId)
            return
        }

        val currentItems = _cartItems.value?.toMutableList() ?: return
        val itemIndex = currentItems.indexOfFirst { it.id == itemId }
        
        if (itemIndex != -1) {
            val item = currentItems[itemIndex]
            val updatedItem = item.copy(
                quantity = newQuantity,
                totalPrice = item.unitPrice.multiply(BigDecimal.valueOf(newQuantity))
            )
            currentItems[itemIndex] = updatedItem
            _cartItems.value = currentItems
            calculateCartTotals()
        }
    }

    fun removeItemFromCart(itemId: Long) {
        val currentItems = _cartItems.value?.toMutableList() ?: return
        currentItems.removeAll { it.id == itemId }
        _cartItems.value = currentItems
        calculateCartTotals()
    }

    fun applyDiscount(percentage: BigDecimal) {
        discountPercentage = percentage
        calculateCartTotals()
    }

    private fun calculateCartTotals() {
        val items = _cartItems.value ?: return
        
        // Calculate subtotal
        val subtotal = items.fold(BigDecimal.ZERO) { acc, item ->
            acc.add(item.totalPrice)
        }
        _cartSubtotal.value = subtotal
        
        // Calculate tax
        val tax = items.fold(BigDecimal.ZERO) { acc, item ->
            if (item.taxRate > BigDecimal.ZERO) {
                acc.add(item.totalPrice.multiply(item.taxRate).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP))
            } else {
                acc
            }
        }
        _cartTax.value = tax
        
        // Calculate discount
        val discount = subtotal.multiply(discountPercentage).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP)
        _cartDiscount.value = discount
        
        // Calculate total
        val total = subtotal.add(tax).subtract(discount)
        _cartTotal.value = total
    }

    fun voidSale() {
        viewModelScope.launch {
            try {
                if (currentSaleId > 0) {
                    saleRepository.voidSale(currentSaleId, "Venta anulada por el usuario", currentUser?.id ?: 0)
                }
                initializeNewSale()
            } catch (e: Exception) {
                _errorMessage.value = "Error al anular venta: ${e.message}"
            }
        }
    }

    fun holdSale() {
        viewModelScope.launch {
            try {
                // Save current sale state for later retrieval
                // This would typically save to a separate "held sales" table
                _errorMessage.value = "Función de venta en espera no implementada aún"
            } catch (e: Exception) {
                _errorMessage.value = "Error al poner en espera: ${e.message}"
            }
        }
    }

    fun clearCart() {
        initializeNewSale()
    }

    fun getCartSummary(): CartSummary {
        val items = _cartItems.value ?: emptyList()
        return CartSummary(
            itemCount = items.size,
            subtotal = _cartSubtotal.value ?: BigDecimal.ZERO,
            tax = _cartTax.value ?: BigDecimal.ZERO,
            discount = _cartDiscount.value ?: BigDecimal.ZERO,
            total = _cartTotal.value ?: BigDecimal.ZERO
        )
    }

    override fun onCleared() {
        super.onCleared()
        // Clean up resources if needed
    }
}

// Data classes for cart management
data class CartItem(
    val id: Long,
    val productId: Long,
    val productName: String,
    val productSku: String,
    val productBarcode: String?,
    val quantity: Double,
    val unit: String,
    val unitPrice: BigDecimal,
    val totalPrice: BigDecimal,
    val taxRate: BigDecimal,
    val costPrice: BigDecimal?
)

data class CartSummary(
    val itemCount: Int,
    val subtotal: BigDecimal,
    val tax: BigDecimal,
    val discount: BigDecimal,
    val total: BigDecimal
)