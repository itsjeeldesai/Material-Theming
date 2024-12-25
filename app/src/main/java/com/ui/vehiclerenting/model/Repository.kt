package com.ui.vehiclerenting.model

import android.content.Context

interface CategoryRepository {
    suspend fun getCategories(context: Context): CategoryResponse
}

class CategoryRepositoryImpl(private val dataSource: DataSource) : CategoryRepository {
    override suspend fun getCategories(context: Context): CategoryResponse {
        return dataSource.fetchCategories(context)
    }
}

// For Car Details from JSON

interface CarDetailsRepository {
    suspend fun getCarDetails(context: Context): CarDetailsResponse
}

class CarDetailsRepositoryImpl(private val dataSource: DataSource) : CarDetailsRepository {
    override suspend fun getCarDetails(context: Context): CarDetailsResponse {
        return dataSource.fetchCarDetails(context)
    }
}