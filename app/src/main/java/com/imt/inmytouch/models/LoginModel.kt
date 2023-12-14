package com.imt.inmytouch.models

import com.google.gson.annotations.SerializedName

// LoginModel.kt
data class LoginModel(
    @SerializedName("email") val emailOrMobile: String,
    @SerializedName("password") val password: String
)
