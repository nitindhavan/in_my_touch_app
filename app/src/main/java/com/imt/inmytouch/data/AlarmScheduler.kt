package com.imt.inmytouch.data

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import android.util.Log
import java.util.concurrent.TimeUnit

class AlarmScheduler(private val context: Context) {

    fun scheduleRepeatingTask() {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, YourAlarmReceiver::class.java)

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val interval = TimeUnit.MINUTES.toMillis(1) // Set your desired interval here

        // Schedule the alarm
        try {
            alarmManager.setRepeating(
                AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime(),
                interval,
                pendingIntent
            )

        } catch (e: Exception) {
            Log.d("response",e.toString())
        }

    }
}
