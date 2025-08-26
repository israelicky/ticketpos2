package com.ticketpos.app.data.dao

import androidx.room.*
import com.ticketpos.app.data.entity.Customer
import kotlinx.coroutines.flow.Flow

@Dao
interface CustomerDao {
    
    @Query("SELECT * FROM customers WHERE isActive = 1 ORDER BY fullName ASC")
    fun getAllActiveCustomers(): Flow<List<Customer>>
    
    @Query("SELECT * FROM customers ORDER BY fullName ASC")
    fun getAllCustomers(): Flow<List<Customer>>
    
    @Query("SELECT * FROM customers WHERE id = :id")
    suspend fun getCustomerById(id: Long): Customer?
    
    @Query("SELECT * FROM customers WHERE customerCode = :customerCode")
    suspend fun getCustomerByCode(customerCode: String): Customer?
    
    @Query("SELECT * FROM customers WHERE phone = :phone")
    suspend fun getCustomerByPhone(phone: String): Customer?
    
    @Query("SELECT * FROM customers WHERE email = :email")
    suspend fun getCustomerByEmail(email: String): Customer?
    
    @Query("SELECT * FROM customers WHERE fullName LIKE '%' || :query || '%' OR phone LIKE '%' || :query || '%' OR email LIKE '%' || :query || '%'")
    fun searchCustomers(query: String): Flow<List<Customer>>
    
    @Query("SELECT * FROM customers WHERE customerType = :type AND isActive = 1 ORDER BY fullName ASC")
    fun getCustomersByType(type: String): Flow<List<Customer>>
    
    @Query("SELECT * FROM customers WHERE currentBalance > 0 AND isActive = 1 ORDER BY currentBalance DESC")
    fun getCustomersWithBalance(): Flow<List<Customer>>
    
    @Query("SELECT COUNT(*) FROM customers WHERE isActive = 1")
    suspend fun getActiveCustomersCount(): Int
    
    @Query("SELECT COUNT(*) FROM customers WHERE customerType = :type AND isActive = 1")
    suspend fun getCustomersCountByType(type: String): Int
    
    @Query("SELECT SUM(currentBalance) FROM customers WHERE isActive = 1")
    suspend fun getTotalCustomerBalance(): Double?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCustomer(customer: Customer): Long
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCustomers(customers: List<Customer>)
    
    @Update
    suspend fun updateCustomer(customer: Customer)
    
    @Delete
    suspend fun deleteCustomer(customer: Customer)
    
    @Query("UPDATE customers SET currentBalance = currentBalance + :amount WHERE id = :customerId")
    suspend fun addToBalance(customerId: Long, amount: Double)
    
    @Query("UPDATE customers SET currentBalance = currentBalance - :amount WHERE id = :customerId AND currentBalance >= :amount")
    suspend fun subtractFromBalance(customerId: Long, amount: Double): Int
    
    @Query("UPDATE customers SET isActive = :isActive, updatedAt = :updatedAt WHERE id = :customerId")
    suspend fun updateActiveStatus(customerId: Long, isActive: Boolean, updatedAt: java.util.Date)
}