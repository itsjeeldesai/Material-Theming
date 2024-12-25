package com.ui.vehiclerenting.presentation.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ui.vehiclerenting.model.CarDetails
import com.ui.vehiclerenting.model.Category
import com.ui.vehiclerenting.domain.GetCarDetailsUseCase
import com.ui.vehiclerenting.domain.GetCategoryUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CategoryViewModel(
    application: Application,
    private val getCategoryUseCase: GetCategoryUseCase,
    private val getCarDetailsUseCase: GetCarDetailsUseCase
) : AndroidViewModel(application) {

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories

    private val _selectedCategoryIndex = MutableStateFlow<Int?>(0)
    val selectedCategoryIndex: StateFlow<Int?> = _selectedCategoryIndex

//    Car Details

    private val _details = MutableStateFlow<List<CarDetails>>(emptyList())
    val carDetails: StateFlow<List<CarDetails>> = _details

    init {
        fetchCategories()
        fetchCarDetails()
    }

    private fun fetchCategories() {
        viewModelScope.launch {
            val categoryResponse = getCategoryUseCase.execute(getApplication())
            _categories.value = categoryResponse.categories
        }
    }

    private fun fetchCarDetails() {
        viewModelScope.launch {
            val carDetailsResponse = getCarDetailsUseCase.execute(getApplication())
            _details.value = carDetailsResponse.carDetails
        }
    }

    fun onCategorySelected(index: Int) {
        _selectedCategoryIndex.value = if (_selectedCategoryIndex.value == index) 0 else index
    }

    fun getFilteredCarDetails(): List<CarDetails> {
        return when (val selectedIndex = _selectedCategoryIndex.value) {
            null -> _details.value
            0 -> _details.value
            else -> _details.value.filter { it.type.equals(_categories.value[selectedIndex].name, ignoreCase = true) }
        }
    }
}


class ViewModelFactory(
    private val application: Application,
    private val getCategoryUseCase: GetCategoryUseCase,
    private val getCarDetailsUseCase: GetCarDetailsUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CategoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CategoryViewModel(application, getCategoryUseCase, getCarDetailsUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}