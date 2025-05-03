package com.elsharif.landmarkrecognition.utils

import android.content.Context
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

/**
 * A wrapper class for File to avoid direct use of File.path
 */
class AppFile private constructor(private val file: File) {
    
    companion object {
        fun fromContext(context: Context, name: String): AppFile {
            return AppFile(File(context.filesDir, name))
        }
        
        fun fromCache(context: Context, name: String): AppFile {
            return AppFile(File(context.cacheDir, name))
        }
    }
    
    fun exists(): Boolean = file.exists()
    
    fun delete(): Boolean = file.delete()
    
    fun createNewFile(): Boolean = file.createNewFile()
    
    fun getAbsolutePath(): String = file.absolutePath
    
    fun getName(): String = file.name
    
    fun length(): Long = file.length()
    
    fun getInputStream(): FileInputStream = FileInputStream(file)
    
    fun getOutputStream(): FileOutputStream = FileOutputStream(file)
    
    fun toFile(): File = file
} 