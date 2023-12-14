package com.imt.inmytouch.data

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.util.Log
import com.google.gson.JsonObject
import com.imt.inmytouch.RestClient
import com.imt.inmytouch.models.BatteryModel
import com.imt.inmytouch.models.LocationBody
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar

class YourAlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        context?.let {
            // Use a Coroutine to perform the background task
            GlobalScope.launch {
                uploadLocation(context)
                logBatteryPercentage(context)
            }
        }
    }

    private fun uploadLocation(context: Context?) {
        Log.d("response", "uploadLocation")
        try {
            LocationUtils(context!!).getCurrentLocation {
                val body = LocationBody(
                    7,
                    SimpleDateFormat("dd-MMM-yyyy HH:mm").format(Calendar.getInstance().time),
                    it!!.longitude,
                    it.latitude
                )
                RestClient.api.addLocation(body).enqueue(object : Callback<JsonObject> {
                    override fun onResponse(
                        call: Call<JsonObject>,
                        response: Response<JsonObject>
                    ) {
                        Log.d("response", response.body().toString())
                    }

                    override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                        Log.d("response", t.message.toString())
                    }

                })
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("response", e.toString())
        }
    }

    private fun logBatteryPercentage(context: Context?) {
        try {
            val batteryIntent = context?.registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
            val batteryLevel: Int = batteryIntent?.let {
                val level = it.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
                val scale = it.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
                (level / scale.toFloat() * 100).toInt()
            } ?: -1

            Log.d("BatteryPercentage", "Battery Percentage: $batteryLevel%")
            RestClient.api.updateBattery(BatteryModel(7,batteryLevel,SimpleDateFormat("dd-MMM-yyyy HH:mm").format(Calendar.getInstance().time))).enqueue(object  : Callback<JsonObject>{
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    Log.d("BatteryPercentage", "Uploaded Battery Percentage: $batteryLevel%")
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                }

            })
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("BatteryPercentage", "Error logging battery percentage: ${e.message}")
        }
    }
}
