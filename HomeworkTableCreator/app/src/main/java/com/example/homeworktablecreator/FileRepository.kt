package com.example.homeworktablecreator

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class FileRepository(private val context: Context) {

    fun saveBitmapToStorage(bitmap: Bitmap, fileName: String): Uri? {
        val filenameWithExt = "$fileName.jpg"
        var uri: Uri? = null

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, filenameWithExt)
                    put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/HomeworkTables")
                }
                val contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                uri = context.contentResolver.insert(contentUri, contentValues)
                uri?.let {
                    val stream: OutputStream? = context.contentResolver.openOutputStream(it)
                    stream?.use { out ->
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
                    }
                }
            } else {
                // Legacy storage
                val imagesDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                val imageFile = File(imagesDir, filenameWithExt)
                FileOutputStream(imageFile).use { out ->
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
                }
                uri = FileProvider.getUriForFile(
                    context,
                    "${context.packageName}.fileprovider",
                    imageFile
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
        return uri
    }

    fun shareImage(uri: Uri) {
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_STREAM, uri)
            type = "image/jpeg"
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        val chooser = Intent.createChooser(shareIntent, "Share Homework Table")
        chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(chooser)
    }
}
