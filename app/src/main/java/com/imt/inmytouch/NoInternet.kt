package com.imt.inmytouch

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast

class NoInternet : AppCompatActivity() {
    lateinit var refreshButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_no_internet)
        refreshButton=findViewById(R.id.refreshButton)
        refreshButton.setOnClickListener {
            if(isConnectedToInternet()){
                startActivity(Intent(this@NoInternet,MainActivity::class.java))
            }
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

        Toast.makeText(this@NoInternet,"Not Connected to Internet",Toast.LENGTH_SHORT).show()
        return false
    }
}