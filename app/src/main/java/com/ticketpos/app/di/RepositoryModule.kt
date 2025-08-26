package com.ticketpos.app.di

import com.ticketpos.app.data.repository.ProductRepository
import com.ticketpos.app.data.repository.SaleRepository
import com.ticketpos.app.data.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideUserRepository(
        userDao: com.ticketpos.app.data.dao.UserDao,
        sharedPreferences: android.content.SharedPreferences
    ): UserRepository {
        return UserRepository(userDao, sharedPreferences)
    }

    @Provides
    @Singleton
    fun provideProductRepository(
        productDao: com.ticketpos.app.data.dao.ProductDao
    ): ProductRepository {
        return ProductRepository(productDao)
    }

    @Provides
    @Singleton
    fun provideSaleRepository(
        saleDao: com.ticketpos.app.data.dao.SaleDao,
        productDao: com.ticketpos.app.data.dao.ProductDao
    ): SaleRepository {
        return SaleRepository(saleDao, productDao)
    }
}
