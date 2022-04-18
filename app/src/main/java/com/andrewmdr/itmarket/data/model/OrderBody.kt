package com.andrewmdr.itmarket.data.model

data class OrderBody(
    val id: String,
    val packageCount: String,
    val address: String,
    val house: String,
    val service: String,
    val date: String,
    val phone: String,
    val building: Int,
    val entrance: Int,
    val floor: Int,
    val name: String,
    val comment: String?,
    val price: Long
)
