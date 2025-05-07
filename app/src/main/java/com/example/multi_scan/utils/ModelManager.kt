package com.example.multi_scan.utils

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.tensorflow.lite.task.core.BaseOptions
import org.tensorflow.lite.task.vision.classifier.ImageClassifier
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.URL

/**
 * Manages TensorFlow Lite models for the app
 * Uses dynamic model loading to reduce APK size
 */
class ModelManager(private val context: Context) {
    
    companion object {
        private const val TAG = "ModelManager"
        
        // URLs for model files (these would point to your actual hosting location)
        private val MODEL_URLS = mapOf(
            "africa" to "https://storage.googleapis.com/your-bucket/africa.tflite",
            "asia" to "https://storage.googleapis.com/your-bucket/asia.tflite",
            "europe" to "https://storage.googleapis.com/your-bucket/europe.tflite", 
            "northamerica" to "https://storage.googleapis.com/your-bucket/northamerica.tflite",
            "southamerica" to "https://storage.googleapis.com/your-bucket/southamerica.tflite",
            "oceania_antarctica" to "https://storage.googleapis.com/your-bucket/oceania_antarctica.tflite"
        )
        
        // Model file names
        val MODEL_NAMES = listOf(
            "africa.tflite", 
            "asia.tflite", 
            "europe.tflite", 
            "northamerica.tflite", 
            "southamerica.tflite", 
            "oceania_antarctica.tflite"
        )
    }
    
    // Status tracking for UI
    val downloadProgress = mutableStateOf(0f)
    val isDownloading = mutableStateOf(false)
    val downloadedModels = mutableStateOf(emptyList<String>())
    
    /**
     * Check if specific model is downloaded
     */
    fun isModelDownloaded(modelName: String): Boolean {
        val modelFile = File(getModelDirectory(), modelName)
        return modelFile.exists()
    }
    
    /**
     * Get all downloaded models
     */
    fun getDownloadedModels(): List<String> {
        val modelDir = getModelDirectory()
        if (!modelDir.exists()) return emptyList()
        
        return modelDir.listFiles()
            ?.filter { it.name.endsWith(".tflite") }
            ?.map { it.name }
            ?: emptyList()
    }
    
    /**
     * Download a model from remote storage
     */
    suspend fun downloadModel(modelName: String): Boolean {
        if (!MODEL_NAMES.contains(modelName)) {
            Log.e(TAG, "Unknown model: $modelName")
            return false
        }
        
        val modelKey = modelName.replace(".tflite", "")
        val modelUrl = MODEL_URLS[modelKey] ?: return false
        
        isDownloading.value = true
        downloadProgress.value = 0f
        
        return withContext(Dispatchers.IO) {
            try {
                val modelFile = File(getModelDirectory(), modelName)
                
                // Create directories if needed
                modelFile.parentFile?.mkdirs()
                
                // Download the file
                val connection = URL(modelUrl).openConnection()
                val totalSize = connection.contentLength.toFloat()
                
                connection.getInputStream().use { input ->
                    FileOutputStream(modelFile).use { output ->
                        val buffer = ByteArray(4096)
                        var bytesRead: Int
                        var downloaded = 0f
                        
                        while (input.read(buffer).also { bytesRead = it } != -1) {
                            output.write(buffer, 0, bytesRead)
                            downloaded += bytesRead
                            downloadProgress.value = downloaded / totalSize
                        }
                    }
                }
                
                // Update the list of downloaded models
                refreshDownloadedModelsList()
                true
            } catch (e: IOException) {
                Log.e(TAG, "Failed to download model: $modelName", e)
                false
            } finally {
                isDownloading.value = false
            }
        }
    }
    
    /**
     * Load a classifier from a model file
     */
    fun loadClassifier(modelName: String): ImageClassifier? {
        if (!isModelDownloaded(modelName)) {
            Log.e(TAG, "Model not downloaded: $modelName")
            return null
        }
        
        try {
            val modelFile = File(getModelDirectory(), modelName)
            val options = ImageClassifier.ImageClassifierOptions.builder()
                .setBaseOptions(BaseOptions.builder().useGpu().build())
                .setMaxResults(3)
                .build()
                
            return ImageClassifier.createFromFileAndOptions(modelFile, options)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to load classifier for model: $modelName", e)
            return null
        }
    }
    
    /**
     * Delete a downloaded model to free up space
     */
    fun deleteModel(modelName: String): Boolean {
        val modelFile = File(getModelDirectory(), modelName)
        val result = modelFile.delete()
        refreshDownloadedModelsList()
        return result
    }
    
    /**
     * Get the model storage directory
     */
    private fun getModelDirectory(): File {
        return File(context.getExternalFilesDir(null), "models")
    }
    
    /**
     * Refresh the list of downloaded models
     */
    private fun refreshDownloadedModelsList() {
        downloadedModels.value = getDownloadedModels()
    }
    
    /**
     * Calculate total size of all downloaded models
     */
    fun getTotalModelSize(): Long {
        val modelDir = getModelDirectory()
        if (!modelDir.exists()) return 0
        
        return modelDir.listFiles()
            ?.filter { it.name.endsWith(".tflite") }
            ?.sumOf { it.length() }
            ?: 0
    }
} 