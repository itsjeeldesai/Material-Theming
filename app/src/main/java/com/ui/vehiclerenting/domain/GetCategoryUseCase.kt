package com.ui.vehiclerenting.domain

import android.content.Context
import com.ui.vehiclerenting.model.CategoryRepository
import com.ui.vehiclerenting.model.CategoryResponse

class GetCategoryUseCase(private val repository: CategoryRepository) {
    suspend fun execute(context: Context): CategoryResponse {
        return repository.getCategories(context)
    }
}
