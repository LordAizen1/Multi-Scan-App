package com.elsharif.landmarkrecognition.models

import android.content.Context
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.elsharif.landmarkrecognition.components.RegionTopBar
import com.elsharif.landmarkrecognition.components.finishActivity
import com.elsharif.landmarkrecognition.data.TFLiteLandmarkClassifierOfNorthAmerica
import com.elsharif.landmarkrecognition.domain.Classification
import com.elsharif.landmarkrecognition.presentation.CameraPreview
import com.elsharif.landmarkrecognition.presentation.LandmarkImageAnalyzer
import com.example.multi_scan.R

@Composable
fun tfLiteLandmarkClassifierOfNorthAmerica(context: Context) {
    var classifications by remember {
        mutableStateOf(emptyList<Classification>())
    }
    
    val analyzer = remember {
        LandmarkImageAnalyzer(
            classifier = TFLiteLandmarkClassifierOfNorthAmerica(
                context = context
            ),
            onResult = {
                classifications = it
            }
        )
    }

    val controller = remember {
        LifecycleCameraController(context).apply {
            setEnabledUseCases(CameraController.IMAGE_ANALYSIS)
            setImageAnalysisAnalyzer(
                ContextCompat.getMainExecutor(context),
                analyzer
            )
        }
    }
    
    Scaffold(
        topBar = {
            RegionTopBar(
                title = stringResource(id = R.string.north_america_name),
                onBackClicked = { context.finishActivity() }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            CameraPreview(controller = controller, Modifier.fillMaxSize())

            // Show landmark recognition results with animations
            AnimatedVisibility(
                visible = classifications.isNotEmpty(),
                enter = fadeIn(animationSpec = tween(300)) +
                        slideInVertically(
                            animationSpec = tween(300),
                            initialOffsetY = { it / 2 }
                        )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .padding(16.dp)
                ) {
                    classifications.forEach {
                        NorthAmericaLandmarkResultCard(classification = it)
                    }
                }
            }
        }
    }
}

@Composable
fun NorthAmericaLandmarkResultCard(classification: Classification) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFB14AF0).copy(alpha = 0.8f),
                        Color(0xFF8F1AE4).copy(alpha = 0.8f)
                    )
                )
            )
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = classification.name,
                textAlign = TextAlign.Center,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.fillMaxWidth()
            )
            
            Text(
                text = "Confidence: ${(classification.score * 100).toInt()}%",
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                color = Color.White.copy(alpha = 0.8f),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp)
            )
        }
    }
}
