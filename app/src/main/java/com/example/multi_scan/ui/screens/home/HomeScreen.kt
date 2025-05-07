package com.example.multi_scan.ui.screens.home

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Language
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import com.example.multi_scan.R
import com.example.multi_scan.data.models.PdfEntity
import com.example.multi_scan.ui.screens.common.ErrorScreen
import com.example.multi_scan.ui.screens.common.LanguageSelectionDialog
import com.example.multi_scan.ui.screens.common.LoadingDialog
import com.example.multi_scan.ui.screens.common.RenameDeleteDialog
import com.example.multi_scan.ui.screens.home.components.PdfLayout
import com.example.multi_scan.ui.viewmodels.PdfViewModel
import com.example.multi_scan.utils.LanguageUtils
import com.example.multi_scan.utils.copyPdffileToAppDirectory
import com.example.multi_scan.utils.getFileSize
import com.example.multi_scan.utils.showToast
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions
import com.google.mlkit.vision.documentscanner.GmsDocumentScanning
import com.google.mlkit.vision.documentscanner.GmsDocumentScanningResult
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

@SuppressLint("ContextCastToActivity")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    pdfViewModel: PdfViewModel,
    onNavigateToDocScanner: () -> Unit,
    onNavigateToLandmarkRecognition: () -> Unit
) {
    val context = LocalContext.current
    var showLanguageDialog by remember { mutableStateOf(false) }
    
    if (showLanguageDialog) {
        LanguageSelectionDialog(
            onDismiss = { showLanguageDialog = false },
            onLanguageSelected = { languageCode ->
                // Save selected language
                LanguageUtils.setLanguage(context, languageCode)
                
                // Update app locale
                LanguageUtils.updateLocale(context, languageCode)
                
                // Restart activity to apply changes
                val intent = (context as Activity).intent
                context.finish()
                context.startActivity(intent)
                
                showLanguageDialog = false
            }
        )
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.app_name))
                },
                actions = {
                    // Language selection button
                    IconButton(onClick = { showLanguageDialog = true }) {
                        Icon(
                            imageVector = Icons.Default.Language,
                            contentDescription = stringResource(id = R.string.change_language),
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(64.dp))
            
            Text(
                text = stringResource(id = R.string.welcome_message),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = stringResource(id = R.string.choose_option),
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                color = Color.Gray
            )
            
            Spacer(modifier = Modifier.height(64.dp))
            
            Button(
                onClick = { onNavigateToDocScanner() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(
                    text = stringResource(id = R.string.document_scanner),
                    fontSize = 18.sp
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Button(
                onClick = { onNavigateToLandmarkRecognition() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                )
            ) {
                Text(
                    text = stringResource(id = R.string.landmark_recognition),
                    fontSize = 18.sp
                )
            }
        }
    }
}

@SuppressLint("ContextCastToActivity")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DocScannerScreen(pdfViewModel: PdfViewModel) {
    LoadingDialog(pdfViewModel = pdfViewModel)
    RenameDeleteDialog(pdfViewModel = pdfViewModel)

    val activity = LocalContext.current as Activity
    val context = LocalContext.current

    val pdfState by pdfViewModel.pdfStateFlow.collectAsState()

    val scannerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult()
    ) {
        result->
        if (result.resultCode == Activity.RESULT_OK){
            val scanningResult = GmsDocumentScanningResult.fromActivityResultIntent(result.data)


            scanningResult?.pdf?.let {pdf->
                Log.d("pdfName", pdf.uri.lastPathSegment.toString())
                val date = Date()
                val fileName = SimpleDateFormat(
                    "dd-MM-yyyy HH:mm:ss",
                    Locale.getDefault()
                ).format(date) + ".pdf"

                copyPdffileToAppDirectory(context, pdf.uri, fileName)

                if (copyPdffileToAppDirectory(context, pdf.uri, fileName)) {
                    val pdfEntity = PdfEntity(UUID.randomUUID().toString(), fileName, getFileSize(context, fileName))
                    pdfViewModel.insertPdf(pdfEntity)
                }
            }
        }
    }

    val scanner = remember{
        GmsDocumentScanning.getClient(
            GmsDocumentScannerOptions.Builder()
                .setGalleryImportAllowed(true)
                .setResultFormats(GmsDocumentScannerOptions.RESULT_FORMAT_PDF)
                .setScannerMode(GmsDocumentScannerOptions.SCANNER_MODE_FULL).build()
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.document_scanner))
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(onClick = {
                scanner.getStartScanIntent(activity).addOnSuccessListener {
                    scannerLauncher.launch(
                        IntentSenderRequest.Builder(it).build()
                    )
                }.addOnFailureListener{
                    it.printStackTrace()
                    context.showToast(it.message.toString())
                }
            }, text = {
                Text(text = stringResource(R.string.scan))
            }, icon = {
                Icon(painter = painterResource(id = R.drawable.baseline_camera_alt_24), contentDescription = null)
            })
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when {
                pdfState.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                pdfState.error.isNotEmpty() -> {
                    ErrorScreen(message = pdfState.error)
                }

                pdfState.pdfList.isEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.no_pdf_found),
                            fontSize = 18.sp
                        )
                    }
                }

                else -> {
                    LazyColumn {
                        items(pdfState.pdfList) { pdfEntity ->
                            PdfLayout(
                                pdfEntity = pdfEntity, 
                                onDelete = {
                                    pdfViewModel.setDeletePdf(it)
                                }, 
                                onRename = {
                                    pdfViewModel.setRenamePdf(it)
                                },
                                onView = { pdfToView ->
                                    // Open PDF using intent
                                    val pdfFile = File(context.filesDir, pdfToView.pdfName)
                                    if (pdfFile.exists()) {
                                        try {
                                            val uri = FileProvider.getUriForFile(
                                                context,
                                                "${context.packageName}.provider",
                                                pdfFile
                                            )
                                            val intent = Intent(Intent.ACTION_VIEW).apply {
                                                setDataAndType(uri, "application/pdf")
                                                flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                                            }
                                            context.startActivity(intent)
                                        } catch (e: Exception) {
                                            context.showToast(context.getString(R.string.cannot_open_pdf, e.message))
                                        }
                                    } else {
                                        context.showToast(context.getString(R.string.pdf_not_found))
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
} 