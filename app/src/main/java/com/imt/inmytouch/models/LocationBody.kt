package com.imt.inmytouch.models

import com.google.gson.annotations.SerializedName

data class LocationBody(

    @SerializedName("ProfileID") val profileId: Int,
    @SerializedName("DateAndTime") val dateAndTime: String, // Assuming you will format the date on the client side
    @SerializedName("Longitude") val longitude: Double,
    @SerializedName("Latitude") val latitude: Double
)
