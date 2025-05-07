package com.example.multi_scan

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.compose.rememberNavController
import com.example.multi_scan.navigation.AppNavHost
import com.example.multi_scan.ui.theme.MultiScanTheme
import com.example.multi_scan.ui.viewmodels.PdfViewModel
import com.example.multi_scan.utils.LanguageUtils

class MainActivity : ComponentActivity() {
    private val pdfViewModel by viewModels<PdfViewModel>{
        viewModelFactory {
            addInitializer(PdfViewModel::class){
                PdfViewModel(application)
            }
        }
    }
    
    // Permission launcher
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            // Permission granted, continue as normal
        } else {
            // Permission denied, show message
            Toast.makeText(
                this,
                "Camera permission is required for scanner features",
                Toast.LENGTH_LONG
            ).show()
        }
    }
    
    override fun attachBaseContext(newBase: Context) {
        // Apply saved language before activity creation
        val languageCode = LanguageUtils.getLanguage(newBase)
        val context = LanguageUtils.updateLocale(newBase, languageCode)
        super.attachBaseContext(context)
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        // Apply saved language
        val languageCode = LanguageUtils.getLanguage(this)
        LanguageUtils.updateLocale(this, languageCode)
        
        // Request camera permission
        requestCameraPermission()
        
        setContent {
            splashScreen.setKeepOnScreenCondition{ pdfViewModel.isSplashScreen }
            MultiScanTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    AppNavHost(navController = navController, pdfViewModel = pdfViewModel)
                }
            }
        }
    }
    
    private fun requestCameraPermission() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                // Permission already granted
            }
            else -> {
                // Request permission
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }
} 