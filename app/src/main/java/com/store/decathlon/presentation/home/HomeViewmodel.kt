package com.store.decathlon.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.store.decathlon.domain.model.ProductModel
import com.store.decathlon.domain.repository.ProductRepository
import com.store.decathlon.util.SortOption
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val productRepository: ProductRepository // Injecting the repository
) : ViewModel() {

    private val _products = MutableStateFlow<List<ProductModel>>(emptyList())
    val products: StateFlow<List<ProductModel>> = _products

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _isLoadingMore = MutableStateFlow(false)
    val isLoadingMore: StateFlow<Boolean> = _isLoadingMore

    var currentPage = 1
    var sortOption: SortOption = SortOption.BY_PRICE

    init {
        fetchProducts(1, 20)
    }

    fun fetchProducts(page: Int, perPage: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val products = productRepository.getPaginatedProducts(page, perPage)
                _products.value = products
            } catch (e: Exception) {
                // Handle error
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun sortProducts(option: SortOption) {
        viewModelScope.launch {
            sortOption = option
            _products.value = when (option) {
                SortOption.BY_PRICE -> _products.value.sortedBy { it.price }
                SortOption.BY_NAME -> _products.value.sortedBy { it.name }
            }
        }
    }

    fun searchProducts(query: String) {
        viewModelScope.launch {
            val filteredProducts = _products.value.filter {
                it.name.contains(query, ignoreCase = true) ||
                        it.brand.contains(query, ignoreCase = true)
            }
            _products.value = filteredProducts
        }
    }
}
