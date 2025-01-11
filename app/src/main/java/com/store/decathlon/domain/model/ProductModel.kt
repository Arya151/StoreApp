package com.store.decathlon.domain.model

data class ProductModel(
    val id: String,
    val name: String,
    val brand: String,
    val price: Float,
    val imageUrl: String
)