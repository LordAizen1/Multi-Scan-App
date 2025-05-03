package com.elsharif.landmarkrecognition.utils

import android.content.Context
import java.io.File

/**
 * Utility class to help with file access without using reflection
 */
object FileAccessWorkaround {
    
    /**
     * Get a temporary file without relying on File.path field
     */
    fun getTempFile(context: Context, prefix: String, suffix: String): File {
        // Use app's cache directory instead of system temp dir
        val cacheDir = context.cacheDir
        val fileName = "$prefix${System.currentTimeMillis()}$suffix"
        return File(cacheDir, fileName)
    }
    
    /**
     * Create file in app's files directory rather than using system paths
     */
    fun createFile(context: Context, fileName: String): File {
        return File(context.filesDir, fileName)
    }
} 