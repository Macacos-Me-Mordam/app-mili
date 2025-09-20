package com.example.appfetin.model

import com.google.gson.annotations.SerializedName

data class Occurrence(
    @SerializedName("description") val description: String,
    @SerializedName("photo_url") val photoUrl: String,
    @SerializedName("address") val address: String,
    @SerializedName("frequency") val frequency: String
)

data class LoginRequest(
    val email: String,
    val password: String
)