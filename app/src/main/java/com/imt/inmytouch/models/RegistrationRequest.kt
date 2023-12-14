package com.imt.inmytouch.models

import com.google.gson.annotations.SerializedName

data class RegistrationRequest(
    @SerializedName("id") val id: Int,
    @SerializedName("email") val email: String,
    @SerializedName("mobile") val mobile: String,
    @SerializedName("imei") val imei: String,
    @SerializedName("name") val name: String,
    @SerializedName("gender") val gender: String,
    @SerializedName("about") val about: String,
    @SerializedName("password") val password: String
)
