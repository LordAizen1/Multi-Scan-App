package com.elsharif.landmarkrecognition

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.elsharif.landmarkrecognition.data.TFLiteLandmarkClassifierOfEurope
import com.elsharif.landmarkrecognition.domain.Classification
import com.elsharif.landmarkrecognition.models.tfLiteLandmarkClassifierOfAfrica
import com.elsharif.landmarkrecognition.models.tfLiteLandmarkClassifierOfAsia
import com.elsharif.landmarkrecognition.models.tfLiteLandmarkClassifierOfEurope
import com.elsharif.landmarkrecognition.models.tfLiteLandmarkClassifierOfNorthAmerica
import com.elsharif.landmarkrecognition.models.tfLiteLandmarkClassifierOfOceaniaAntarctica
import com.elsharif.landmarkrecognition.models.tfLiteLandmarkClassifierOfSouthAmerica
import com.elsharif.landmarkrecognition.presentation.CameraPreview
import com.elsharif.landmarkrecognition.presentation.LandmarkImageAnalyzer
import com.elsharif.landmarkrecognition.ui.theme.LandmarkRecognitionTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(!hasCameraPermission()){
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.CAMERA),0
            )
        }

        setContent {

            LandmarkRecognitionTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFF7B8937)
                ) {

                    // Set up the Navigation host
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = "home"
                    ) {
                        composable("home") {
                            HomeScreen(navController)
                        }
                        composable("africa") {

                            tfLiteLandmarkClassifierOfAfrica(context = applicationContext)
                        }
                        composable("asia") {
                            tfLiteLandmarkClassifierOfAsia(context = applicationContext)

                        }
                        composable("europe") {
                            tfLiteLandmarkClassifierOfEurope(context = applicationContext)

                        }
                        composable("antarctica") {
                            tfLiteLandmarkClassifierOfOceaniaAntarctica(context = applicationContext)

                        }
                        composable("southamerica") {
                            tfLiteLandmarkClassifierOfSouthAmerica(context = applicationContext)

                        }
                        composable("northamerica") {
                            tfLiteLandmarkClassifierOfNorthAmerica(context = applicationContext)

                        }

                    }
                }
        }
        }

    }
    private fun hasCameraPermission()=ContextCompat.checkSelfPermission(
        this,Manifest.permission.CAMERA
    )==PackageManager.PERMISSION_GRANTED

}