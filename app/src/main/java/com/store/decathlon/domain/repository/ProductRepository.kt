package com.store.decathlon.domain.repository

import com.store.decathlon.domain.model.ProductModel
import com.store.decathlon.util.SortOption

interface ProductRepository {
    suspend fun getPaginatedProducts(page: Int, perPage: Int): List<ProductModel>
    suspend fun getSortedProducts(sortOption: SortOption): List<ProductModel>
    suspend fun searchProducts(query: String): List<ProductModel>
}