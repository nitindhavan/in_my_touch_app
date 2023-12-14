package com.imt.inmytouch

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.gson.JsonObject
import com.imt.inmytouch.data.AlarmScheduler
import com.imt.inmytouch.data.LocationUtils
import com.imt.inmytouch.models.LocationBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.security.Permission
import java.text.SimpleDateFormat
import java.util.Calendar

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS)
        intent.data = Uri.parse("package:" + packageName)
        startActivity(intent)

        if(ActivityCompat.checkSelfPermission(
            this@MainActivity,
            Manifest.permission.ACCESS_COARSE_LOCATION)==PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this@MainActivity,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            ) == PackageManager.PERMISSION_GRANTED) {
            Log.d("response","response")
            AlarmScheduler(this@MainActivity).scheduleRepeatingTask()
            if (!isConnectedToInternet()) {
                // Your code when not connected to the internet
                startActivity(Intent(this@MainActivity,NoInternet::class.java))
                Toast.makeText(this, "Not connected to the internet", Toast.LENGTH_SHORT).show()
            }

            val sharedPreferences=getSharedPreferences("sharedp", MODE_PRIVATE)
            val id=sharedPreferences.getInt("uid",-1)
            if(id==-1){
                startActivity(Intent(this@MainActivity,LoginActivity::class.java))
                finish()
            }else{
                val intent =Intent(this@MainActivity,HomeActivity::class.java)
                intent.putExtra("userID",id)
                startActivity(intent)
                finish()
            }
        }else{
            ActivityCompat.requestPermissions(
                this@MainActivity,
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_BACKGROUND_LOCATION),
                100
            )
        }
    }

    private fun isConnectedToInternet(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val network = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(network)
        if (capabilities != null) {
            return (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))
        }

        return false
    }

}