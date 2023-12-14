package com.imt.inmytouch.data

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Build
import android.provider.CallLog
import android.util.Log
import androidx.core.content.ContextCompat

class CallLogger {

    fun logCalls(context: Context) {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_CALL_LOG
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Request permission if not granted
            // Handle the permission request in your activity or fragment
            Log.d("CallLogger", "READ_CALL_LOG permission not granted")
            return
        }

        // Get all calls
        val callsCursor: Cursor? =
            context.contentResolver.query(
                CallLog.Calls.CONTENT_URI,
                null,
                null,
                null,
                null
            )

        callsCursor?.use { cursor ->
            val numberColumnIndex = cursor.getColumnIndex(CallLog.Calls.NUMBER)
            val typeColumnIndex = cursor.getColumnIndex(CallLog.Calls.TYPE)
            val dateColumnIndex = cursor.getColumnIndex(CallLog.Calls.DATE)

            while (cursor.moveToNext()) {
                val phoneNumber = cursor.getString(numberColumnIndex)
                val callType = cursor.getInt(typeColumnIndex)
                val callDate = cursor.getLong(dateColumnIndex)

                val callTypeStr = when (callType) {
                    CallLog.Calls.INCOMING_TYPE -> "Incoming"
                    CallLog.Calls.OUTGOING_TYPE -> "Outgoing"
                    CallLog.Calls.MISSED_TYPE -> "Missed"
                    else -> "Unknown"
                }

                Log.d("CallLogger", "Phone Number: $phoneNumber, Type: $callTypeStr, Date: $callDate")
            }
        }
    }
}
