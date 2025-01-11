package com.store.decathlon.data

import com.store.decathlon.data.remote.NetworkApi
import com.store.decathlon.domain.model.ProductModel
import com.store.decathlon.domain.repository.ProductRepository
import com.store.decathlon.util.SortOption
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val networkApi: NetworkApi
) : ProductRepository {

    // Fetch products in a paginated manner
    override suspend fun getPaginatedProducts(page: Int, perPage: Int): List<ProductModel> {
        return try {
            val response = networkApi.getPaginatedProducts(page, perPage)
            response.map {
                ProductModel(
                    id = it.id,
                    name = it.color,
                    price = it.likes.toFloat(),
                    imageUrl = it.urls.regular,
                    brand = it.description
                )
            }
        } catch (e: Exception) {
            // Handle errors
            emptyList()
        }
    }

    // Fetch sorted products by either price or name
    override suspend fun getSortedProducts(sortOption: SortOption): List<ProductModel> {
        return try {
            val response =
                networkApi.getPaginatedProducts(1, 100)  // Get all items for sorting purposes
            val sortedList = when (sortOption) {
                SortOption.BY_PRICE -> response.sortedBy { it.likes }
                SortOption.BY_NAME -> response.sortedBy { it.color }
            }
            sortedList.map {
                ProductModel(
                    id = it.id,
                    name = it.color,
                    price = it.likes.toFloat(),
                    imageUrl = it.urls.regular,
                    brand = it.description
                )
            }
        } catch (e: Exception) {
            // Handle errors
            emptyList()
        }
    }

    // Search products by name or brand
    override suspend fun searchProducts(query: String): List<ProductModel> {
        return try {
            val response = networkApi.searchProductsByNameOrBrand(
                query,
                1,
                100
            )
            response.filter {
                it.color.contains(query, ignoreCase = true) || it.description.contains(
                    query,
                    ignoreCase = true
                )
            }.map {
                ProductModel(
                    id = it.id,
                    name = it.color,
                    price = it.likes.toFloat(),
                    imageUrl = it.urls.regular,
                    brand = it.description
                )
            }
        } catch (e: Exception) {
            // Handle errors
            emptyList()
        }
    }
}