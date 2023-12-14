package com.imt.inmytouch.models

import com.google.gson.annotations.SerializedName

data class PostImage(
    @SerializedName("userId") var userId: Int,
    @SerializedName("image") var image: String,
    @SerializedName("postCaption") var caption: String
)
