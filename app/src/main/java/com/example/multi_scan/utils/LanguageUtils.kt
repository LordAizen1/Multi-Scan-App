package com.example.multi_scan.utils

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.preference.PreferenceManager
import java.util.Locale

object LanguageUtils {
    private const val LANGUAGE_KEY = "selected_language"
    
    // Language codes
    const val ENGLISH = "en"
    const val BENGALI = "bn"
    const val HINDI = "hi"
    const val URDU = "ur"
    
    // Get saved language code from SharedPreferences
    fun getLanguage(context: Context): String {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        return prefs.getString(LANGUAGE_KEY, ENGLISH) ?: ENGLISH
    }
    
    // Save language code to SharedPreferences
    fun setLanguage(context: Context, languageCode: String) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        prefs.edit().putString(LANGUAGE_KEY, languageCode).apply()
    }
    
    // Update the app locale and return the context
    fun updateLocale(context: Context, languageCode: String): Context {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        
        val resources = context.resources
        val config = Configuration(resources.configuration)
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            config.setLocale(locale)
        } else {
            @Suppress("DEPRECATION")
            config.locale = locale
        }
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val updatedContext = context.createConfigurationContext(config)
            @Suppress("DEPRECATION")
            resources.updateConfiguration(config, resources.displayMetrics)
            return updatedContext
        } else {
            @Suppress("DEPRECATION")
            resources.updateConfiguration(config, resources.displayMetrics)
            return context
        }
    }
    
    // Get a localized Resources instance
    fun getLocalizedResources(context: Context, languageCode: String): Resources {
        val locale = Locale(languageCode)
        val config = Configuration(context.resources.configuration)
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            config.setLocale(locale)
        } else {
            @Suppress("DEPRECATION")
            config.locale = locale
        }
        
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            context.createConfigurationContext(config).resources
        } else {
            val resources = context.resources
            @Suppress("DEPRECATION")
            resources.updateConfiguration(config, resources.displayMetrics)
            resources
        }
    }
} 