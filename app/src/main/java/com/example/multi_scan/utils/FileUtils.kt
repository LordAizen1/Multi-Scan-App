package com.example.multi_scan.utils

import android.content.Context
import android.net.Uri
import android.widget.Toast
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

fun getFileSize(context: Context, fileName: String): String {
    val file = File(context.filesDir, fileName)
    val size = file.length()
    
    return when {
        size < 1024 -> "$size B"
        size < 1024 * 1024 -> "${size / 1024} KB"
        else -> "${size / (1024 * 1024)} MB"
    }
}

fun copyPdffileToAppDirectory(context: Context, uri: Uri, fileName: String): Boolean {
    return try {
        val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
        val outputFile = File(context.filesDir, fileName)
        
        if (inputStream != null) {
            val outputStream = FileOutputStream(outputFile)
            inputStream.use { input ->
                outputStream.use { output ->
                    input.copyTo(output)
                }
            }
            true
        } else {
            false
        }
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}

fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
} 