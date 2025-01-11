package com.store.decathlon.data.remote

import com.store.decathlon.data.remote.dto.ProductResponseDto
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface NetworkApi {

    @Headers("Authorization: Client-ID $TOKEN")
    @GET("/photos")
    suspend fun getPaginatedProducts(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): List<ProductResponseDto>

    @GET("/search/photos")
    suspend fun searchProductsByNameOrBrand(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): List<ProductResponseDto>

    companion object {
        const val BASE_URL = "https://api.unsplash.com/"
        const val TOKEN = "WZtKfdYwW5dTM4GLhzT7dE_kneAKRZxQFkH1TkrseDc"
    }
}