package com.imt.inmytouch.data

import android.app.Notification
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import androidx.core.content.ContextCompat

class NotificationLogger : NotificationListenerService() {

    override fun onNotificationPosted(sbn: StatusBarNotification) {

        val notification: Notification = sbn.notification

        // Access notification details
        val appName = notification.extras?.getString(Notification.EXTRA_TITLE)
        val text = notification.extras?.getCharSequence(Notification.EXTRA_TEXT)
        val tickerText = notification.tickerText

        Log.d("NotificationLogger", "App: $appName, Text: $text, Ticker: $tickerText")
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        // Handle notification removal if needed
    }
}
