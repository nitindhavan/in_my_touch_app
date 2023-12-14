package com.imt.inmytouch.models

import com.google.gson.annotations.SerializedName

data class BatteryModel(
    @SerializedName("ProfileID") var profileID: Int,
    @SerializedName("percentage") var percentage: Int,
    @SerializedName("DateAndTime") var dateAndTime: String,
)
