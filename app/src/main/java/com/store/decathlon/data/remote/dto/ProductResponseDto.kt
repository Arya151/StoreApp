package com.store.decathlon.data.remote.dto

data class ProductResponseDto(
    val color: String,
    val description: String,
    val id: String,
    val likes: Int,
    val urls: Urls
)

data class Urls(
    val regular: String
)