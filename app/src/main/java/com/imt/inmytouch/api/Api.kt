package com.imt.inmytouch

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.imt.inmytouch.models.BatteryModel
import com.imt.inmytouch.models.LocationBody
import com.imt.inmytouch.models.LoginModel
import com.imt.inmytouch.models.LoginResponse
import com.imt.inmytouch.models.MessageResponse
import com.imt.inmytouch.models.PostImage
import com.imt.inmytouch.models.RegistrationRequest
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.http.*

/**
 * Contains all server requests
 */
interface Api {

    @POST("user/upload-location")
    fun addLocation(@Body locationBody : LocationBody): Call<JsonObject>

    @POST("user/login")
    fun login(@Body loginBody : LoginModel): Call<LoginResponse>

    @POST("user/register")
    fun registerUser(@Body registrationRequest: RegistrationRequest): Call<MessageResponse>

    @POST("user/upload-post-image")
    fun uploadImage(@Body upload: PostImage): Call<JsonObject>

    @POST("user/upload-battery")
    fun updateBattery(@Body upload: BatteryModel): Call<JsonObject>
}