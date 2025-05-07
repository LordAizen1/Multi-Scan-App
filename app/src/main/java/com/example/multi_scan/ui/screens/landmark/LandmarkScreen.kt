package com.example.multi_scan.ui.screens.landmark

import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.multi_scan.R
import com.elsharif.landmarkrecognition.models.tfLiteLandmarkClassifierOfAfrica
import com.elsharif.landmarkrecognition.models.tfLiteLandmarkClassifierOfAsia
import com.elsharif.landmarkrecognition.models.tfLiteLandmarkClassifierOfEurope
import com.elsharif.landmarkrecognition.models.tfLiteLandmarkClassifierOfNorthAmerica
import com.elsharif.landmarkrecognition.models.tfLiteLandmarkClassifierOfOceaniaAntarctica
import com.elsharif.landmarkrecognition.models.tfLiteLandmarkClassifierOfSouthAmerica

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LandmarkScreen() {
    val context = LocalContext.current
    val navController = rememberNavController()
    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }
    
    // Check permission every time the screen becomes active
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                hasCameraPermission = ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
    
    // Show toast if no camera permission
    LaunchedEffect(hasCameraPermission) {
        if (!hasCameraPermission) {
            Toast.makeText(
                context,
                context.getString(R.string.camera_permission_toast),
                Toast.LENGTH_LONG
            ).show()
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.landmark_recognition),
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "region_selection",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("region_selection") {
                RegionSelectionScreen(navController)
            }
            
            composable("europe") {
                ModelScreenWrapper(hasCameraPermission) {
                    tfLiteLandmarkClassifierOfEurope(context = context)
                }
            }
            
            composable("africa") {
                ModelScreenWrapper(hasCameraPermission) {
                    tfLiteLandmarkClassifierOfAfrica(context = context)
                }
            }
            
            composable("asia") {
                ModelScreenWrapper(hasCameraPermission) {
                    tfLiteLandmarkClassifierOfAsia(context = context)
                }
            }
            
            composable("antarctica") {
                ModelScreenWrapper(hasCameraPermission) {
                    tfLiteLandmarkClassifierOfOceaniaAntarctica(context = context)
                }
            }
            
            composable("southamerica") {
                ModelScreenWrapper(hasCameraPermission) {
                    tfLiteLandmarkClassifierOfSouthAmerica(context = context)
                }
            }
            
            composable("northamerica") {
                ModelScreenWrapper(hasCameraPermission) {
                    tfLiteLandmarkClassifierOfNorthAmerica(context = context)
                }
            }
        }
    }
}

@Composable
fun ModelScreenWrapper(hasCameraPermission: Boolean, content: @Composable () -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        if (hasCameraPermission) {
            content()
        } else {
            // No camera permission UI
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .align(Alignment.Center)
                    .padding(16.dp),
                elevation = CardDefaults.cardElevation(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Column(
                    modifier = Modifier
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = stringResource(id = R.string.camera_permission_required),
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        text = stringResource(id = R.string.camera_permission_message),
                        textAlign = TextAlign.Center,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }
        }
    }
}

@Composable
fun RegionSelectionScreen(navController: NavController) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header with nice background
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                elevation = CardDefaults.cardElevation(4.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Text(
                    text = stringResource(id = R.string.choose_region),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(24.dp),
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Region buttons in a column with animations
            AnimatedVisibility(
                visible = true,
                enter = fadeIn(animationSpec = tween(durationMillis = 500)) +
                    slideInVertically(animationSpec = tween(durationMillis = 500)) { it / 2 }
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    RegionButton(
                        text = stringResource(id = R.string.europ_name),
                        onClick = { navController.navigate("europe") },
                        gradient = Brush.horizontalGradient(
                            listOf(Color(0xFF4A80F0), Color(0xFF1A53E4))
                        )
                    )
                    
                    RegionButton(
                        text = stringResource(id = R.string.africa_name),
                        onClick = { navController.navigate("africa") },
                        gradient = Brush.horizontalGradient(
                            listOf(Color(0xFFF0B14A), Color(0xFFE48F1A))
                        )
                    )
                    
                    RegionButton(
                        text = stringResource(id = R.string.asia_name),
                        onClick = { navController.navigate("asia") },
                        gradient = Brush.horizontalGradient(
                            listOf(Color(0xFF4AF0B1), Color(0xFF1AE48F))
                        )
                    )
                    
                    RegionButton(
                        text = stringResource(id = R.string.anterctica_name),
                        onClick = { navController.navigate("antarctica") },
                        gradient = Brush.horizontalGradient(
                            listOf(Color(0xFF4AA4F0), Color(0xFF1A8AE4))
                        )
                    )
                    
                    RegionButton(
                        text = stringResource(id = R.string.south_america_name),
                        onClick = { navController.navigate("southamerica") },
                        gradient = Brush.horizontalGradient(
                            listOf(Color(0xFFF04A80), Color(0xFFE41A53))
                        )
                    )
                    
                    RegionButton(
                        text = stringResource(id = R.string.north_america_name),
                        onClick = { navController.navigate("northamerica") },
                        gradient = Brush.horizontalGradient(
                            listOf(Color(0xFFB14AF0), Color(0xFF8F1AE4))
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun RegionButton(text: String, onClick: () -> Unit, gradient: Brush) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .height(64.dp)
            .clip(RoundedCornerShape(12.dp)),
        contentPadding = PaddingValues(0.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(gradient),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color.White
            )
        }
    }
} 