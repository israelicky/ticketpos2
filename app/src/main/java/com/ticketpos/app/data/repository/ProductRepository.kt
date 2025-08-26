package com.ticketpos.app.data.repository

import com.ticketpos.app.data.dao.ProductDao
import com.ticketpos.app.data.entity.Product
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductRepository @Inject constructor(
    private val productDao: ProductDao
) {

    suspend fun getAllProducts(): Flow<List<Product>> {
        return productDao.getAllProducts()
    }

    suspend fun getProductById(id: Long): Product? {
        return productDao.getProductById(id)
    }

    suspend fun getProductByBarcode(barcode: String): Product? {
        return productDao.getProductByBarcode(barcode)
    }

    suspend fun searchProducts(query: String): List<Product> {
        return productDao.searchProducts("%$query%")
    }

    suspend fun getProductsByCategory(category: String): Flow<List<Product>> {
        return productDao.getProductsByCategory(category)
    }

    suspend fun getActiveProducts(): Flow<List<Product>> {
        return productDao.getActiveProducts()
    }

    suspend fun getLowStockProducts(threshold: Int = 10): Flow<List<Product>> {
        return productDao.getLowStockProducts(threshold)
    }

    suspend fun insertProduct(product: Product): Long {
        return productDao.insertProduct(product)
    }

    suspend fun updateProduct(product: Product) {
        productDao.updateProduct(product)
    }

    suspend fun deleteProduct(product: Product) {
        productDao.deleteProduct(product)
    }

    suspend fun updateStock(productId: Long, newStock: Int) {
        productDao.updateStock(productId, newStock)
    }

    suspend fun decreaseStock(productId: Long, quantity: Int) {
        val product = getProductById(productId)
        product?.let {
            val newStock = maxOf(0, it.stock - quantity)
            updateStock(productId, newStock)
        }
    }

    suspend fun increaseStock(productId: Long, quantity: Int) {
        val product = getProductById(productId)
        product?.let {
            val newStock = it.stock + quantity
            updateStock(productId, newStock)
        }
    }

    suspend fun getProductCategories(): List<String> {
        return productDao.getProductCategories()
    }

    suspend fun createSampleProducts() {
        // Create sample products if database is empty
        val existingProducts = productDao.getProductCount()
        if (existingProducts == 0) {
            val sampleProducts = listOf(
                Product(
                    id = 0,
                    name = "Coca Cola 500ml",
                    description = "Bebida gaseosa sabor cola",
                    price = 2.50,
                    stock = 100,
                    category = "Bebidas",
                    barcode = "7501234567890",
                    isActive = true,
                    createdAt = System.currentTimeMillis(),
                    updatedAt = System.currentTimeMillis()
                ),
                Product(
                    id = 0,
                    name = "Pan Integral",
                    description = "Pan integral de trigo",
                    price = 3.75,
                    stock = 50,
                    category = "Panadería",
                    barcode = "7501234567891",
                    isActive = true,
                    createdAt = System.currentTimeMillis(),
                    updatedAt = System.currentTimeMillis()
                ),
                Product(
                    id = 0,
                    name = "Leche Entera 1L",
                    description = "Leche entera pasteurizada",
                    price = 4.25,
                    stock = 75,
                    category = "Lácteos",
                    barcode = "7501234567892",
                    isActive = true,
                    createdAt = System.currentTimeMillis(),
                    updatedAt = System.currentTimeMillis()
                )
            )
            
            sampleProducts.forEach { product ->
                insertProduct(product)
            }
        }
    }
}
