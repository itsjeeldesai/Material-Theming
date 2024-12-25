package com.ui.vehiclerenting.domain

import android.content.Context
import com.ui.vehiclerenting.model.CarDetailsRepository
import com.ui.vehiclerenting.model.CarDetailsResponse

class GetCarDetailsUseCase(private val repository: CarDetailsRepository) {
    suspend fun execute(context: Context): CarDetailsResponse {
        return repository.getCarDetails(context)
    }
}
