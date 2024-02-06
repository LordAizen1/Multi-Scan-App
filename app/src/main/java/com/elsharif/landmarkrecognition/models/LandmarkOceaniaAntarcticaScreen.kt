package com.elsharif.landmarkrecognition.models

import android.content.Context
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.elsharif.landmarkrecognition.R
import com.elsharif.landmarkrecognition.data.TFLiteLandmarkClassifierOfAfrica
import com.elsharif.landmarkrecognition.data.TFLiteLandmarkClassifierOfEurope
import com.elsharif.landmarkrecognition.data.TFLiteLandmarkClassifierOfOceaniaAntarctica
import com.elsharif.landmarkrecognition.domain.Classification
import com.elsharif.landmarkrecognition.presentation.CameraPreview
import com.elsharif.landmarkrecognition.presentation.LandmarkImageAnalyzer

@Composable
fun tfLiteLandmarkClassifierOfOceaniaAntarctica(context:Context){

    var classifications by remember{
        mutableStateOf(emptyList<Classification>())
    }
    val analyzer = remember{
        LandmarkImageAnalyzer(
            classifier = TFLiteLandmarkClassifierOfOceaniaAntarctica(
                context = context
            ),
            onResult = {
                classifications=it

            }
        )
    }

    val controller= remember{
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
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp)
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.anterctica_name),
                    modifier = Modifier.weight(1f),
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onPrimary
                )


            }
        }


    ) {paddingValues->

        Box(
            modifier = Modifier.fillMaxSize()
                .padding(paddingValues),

            ){
            CameraPreview(controller =controller, Modifier.fillMaxSize())

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
            ) {
                classifications.forEach {
                    Text(text =it.name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .padding(8.dp),
                        textAlign = TextAlign.Center,
                        fontSize = 20.sp,
                        color= MaterialTheme.colorScheme.primary
                    )
                }

            }


        }


    }



}
