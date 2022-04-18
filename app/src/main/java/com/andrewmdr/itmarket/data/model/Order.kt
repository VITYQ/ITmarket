package com.andrewmdr.itmarket.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.Serializable

@Parcelize
data class Order(
    val id: String,
    val packageCount: Int,
    val address: String,
    val house: Int,
    val building: Int,
    val entrance: Int,
    val floor: Int,
    val service: String,
    val date: String,
    val phone: String,
    val comment: String,
    val price: String,
    var orderStatus: String,
    val name: String?,
    val status: String?
) : Parcelable