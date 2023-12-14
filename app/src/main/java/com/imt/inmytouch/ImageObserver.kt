package com.imt.inmytouch

import android.content.ContentResolver
import android.content.Context
import android.database.ContentObserver
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Handler
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import com.google.gson.JsonObject
import com.imt.inmytouch.models.PostImage
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream

class ImageObserver(private val context: Context, handler: Handler) : ContentObserver(handler) {

    override fun onChange(selfChange: Boolean, uri: Uri?) {
        super.onChange(selfChange, uri)
        Log.d("imagePath","observing")
        if (uri != null && uri.toString().startsWith(MediaStore.Images.Media.EXTERNAL_CONTENT_URI.toString())) {
            // New image added, handle it
            val imagePath = getImagePathFromUri(uri)
            if (imagePath != null) {
                Log.d("imagePath",imagePath)
                RestClient.api.uploadImage(PostImage(7,convertBitmapToBase64(uri),"caption")).enqueue(object : Callback<JsonObject>{
                    override fun onResponse(
                        call: Call<JsonObject>,
                        response: Response<JsonObject>
                    ) {

                    }

                    override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    }

                })
                // Trigger your image upload logic here
                // Example: UploadManager.getInstance().uploadImage(imagePath)
            }
        }
    }

    private fun convertBitmapToBase64(uri: Uri): String {
        val bytes = context.contentResolver.openInputStream(uri)!!.readBytes()

        return Base64.encodeToString(bytes, Base64.DEFAULT)
    }

    private fun getImagePathFromUri(uri: Uri): String? {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        context.contentResolver.query(uri, projection, null, null, null)?.use { cursor ->
            val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            return cursor.getString(columnIndex)
        }
        return null
    }
}
