package com.imt.inmytouch.models

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("message") val message: String,
    @SerializedName("id") val id: Int
)
