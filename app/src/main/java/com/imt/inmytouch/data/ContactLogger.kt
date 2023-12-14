package com.imt.inmytouch.data

import android.Manifest
import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Build
import android.provider.ContactsContract
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class ContactLogger {

    fun logContacts(context: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_CONTACTS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Request permission if not granted
            ActivityCompat.requestPermissions(
                context,
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_BACKGROUND_LOCATION),
                100
            )
            // Handle the permission request in your activity or fragment
            Log.d("ContactLogger", "READ_CONTACTS permission not granted")
            return
        }

        // Get all contacts
        val contactsCursor: Cursor? =
            context.contentResolver.query(
                ContactsContract.Contacts.CONTENT_URI,
                null,
                null,
                null,
                null
            )

        contactsCursor?.use { cursor ->
            val nameColumnIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)

            while (cursor.moveToNext()) {
                val contactName = cursor.getString(nameColumnIndex)
                Log.d("ContactLogger", "Contact: $contactName")
            }
        }
    }
}
