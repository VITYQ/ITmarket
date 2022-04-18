package com.andrewmdr.itmarket.data.model

data class UserInfo(
    val login: String,
    val phone: String,
    val firstName: String,
    val lastName: String,
    val companyDTO: CompanyDTO?
)

