package com.ticketpos.app.data.repository

import com.ticketpos.app.data.dao.ProductDao
import com.ticketpos.app.data.dao.SaleDao
import com.ticketpos.app.data.entity.Sale
import com.ticketpos.app.data.entity.SaleItem
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SaleRepository @Inject constructor(
    private val saleDao: SaleDao,
    private val productDao: ProductDao
) {

    suspend fun getAllSales(): Flow<List<Sale>> {
        return saleDao.getAllSales()
    }

    suspend fun getSaleById(id: Long): Sale? {
        return saleDao.getSaleById(id)
    }

    suspend fun getSalesByDateRange(startDate: Long, endDate: Long): Flow<List<Sale>> {
        return saleDao.getSalesByDateRange(java.util.Date(startDate), java.util.Date(endDate))
    }

    suspend fun getSalesByUser(userId: Long): Flow<List<Sale>> {
        return saleDao.getSalesByUser(userId)
    }

    suspend fun getTodaySales(): Flow<List<Sale>> {
        val startOfDay = getStartOfDay()
        val endOfDay = getEndOfDay()
        return saleDao.getSalesByDateRange(java.util.Date(startOfDay), java.util.Date(endOfDay))
    }

    suspend fun insertSale(sale: Sale): Long {
        return saleDao.insertSale(sale)
    }

    suspend fun updateSale(sale: Sale) {
        saleDao.updateSale(sale)
    }

    suspend fun deleteSale(sale: Sale) {
        saleDao.deleteSale(sale)
    }

    suspend fun getSaleItems(saleId: Long): Flow<List<SaleItem>> {
        return saleDao.getSaleItems(saleId)
    }

    suspend fun insertSaleItem(saleItem: SaleItem): Long {
        return saleDao.insertSaleItem(saleItem)
    }

    suspend fun updateSaleItem(saleItem: SaleItem) {
        saleDao.updateSaleItem(saleItem)
    }

    suspend fun deleteSaleItem(saleItem: SaleItem) {
        saleDao.deleteSaleItem(saleItem)
    }

    suspend fun processSale(sale: Sale, saleItems: List<SaleItem>): Long {
        // Insert sale
        val saleId = insertSale(sale)
        
        // Insert sale items and update product stock
        saleItems.forEach { item ->
            val saleItemWithId = item.copy(saleId = saleId)
            insertSaleItem(saleItemWithId)
            
            // Decrease product stock
            val product = productDao.getProductById(item.productId)
            product?.let {
                val newStock = maxOf(0, it.stock - item.quantity)
                productDao.updateStock(item.productId, newStock)
            }
        }
        
        return saleId
    }

    suspend fun getTotalSalesAmount(startDate: Long, endDate: Long): Double {
        return saleDao.getTotalSalesAmountInDateRange(java.util.Date(startDate), java.util.Date(endDate)) ?: 0.0
    }

    suspend fun getTotalSalesCount(startDate: Long, endDate: Long): Int {
        return saleDao.getSalesCountInDateRange(java.util.Date(startDate), java.util.Date(endDate))
    }

    suspend fun getTodayTotalSales(): Double {
        val startOfDay = getStartOfDay()
        val endOfDay = getEndOfDay()
        return getTotalSalesAmount(startOfDay, endOfDay)
    }

    suspend fun getTodaySalesCount(): Int {
        val startOfDay = getStartOfDay()
        val endOfDay = getEndOfDay()
        return getTotalSalesCount(startOfDay, endOfDay)
    }

    private fun getStartOfDay(): Long {
        val calendar = java.util.Calendar.getInstance()
        calendar.set(java.util.Calendar.HOUR_OF_DAY, 0)
        calendar.set(java.util.Calendar.MINUTE, 0)
        calendar.set(java.util.Calendar.SECOND, 0)
        calendar.set(java.util.Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }

    private fun getEndOfDay(): Long {
        val calendar = java.util.Calendar.getInstance()
        calendar.set(java.util.Calendar.HOUR_OF_DAY, 23)
        calendar.set(java.util.Calendar.MINUTE, 59)
        calendar.set(java.util.Calendar.SECOND, 59)
        calendar.set(java.util.Calendar.MILLISECOND, 999)
        return calendar.timeInMillis
    }
}
