package com.imt.inmytouch.models

import com.google.gson.annotations.SerializedName

data class MessageResponse(
    @SerializedName("message") val message: String,
    @SerializedName("error") val error:String
)
