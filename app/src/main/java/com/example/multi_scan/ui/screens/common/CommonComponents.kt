package com.example.multi_scan.ui.screens.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.multi_scan.data.models.PdfEntity
import com.example.multi_scan.ui.viewmodels.PdfViewModel
import com.example.multi_scan.ui.viewmodels.setShowDialogState
import com.example.multi_scan.ui.viewmodels.setShowRenameDeleteDialogState

@Composable
fun ErrorScreen(message: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message,
            textAlign = TextAlign.Center,
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.error
        )
    }
}

@Composable
fun LoadingDialog(pdfViewModel: PdfViewModel) {
    if (pdfViewModel.showDialog) {
        AlertDialog(
            onDismissRequest = { pdfViewModel.setShowDialogState(false) },
            title = { Text(text = "Processing") },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = "Please wait while processing...")
                }
            },
            confirmButton = {}
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RenameDeleteDialog(pdfViewModel: PdfViewModel) {
    if (pdfViewModel.showRenameDeleteDialog) {
        var newName by remember { mutableStateOf(pdfViewModel.newPdfName) }
        
        AlertDialog(
            onDismissRequest = { pdfViewModel.setShowRenameDeleteDialogState(false) },
            title = {
                Text(
                    text = if (pdfViewModel.pdfToRename != null) "Rename PDF" else "Delete PDF"
                )
            },
            text = {
                if (pdfViewModel.pdfToRename != null) {
                    Column {
                        Text(text = "Enter new name for PDF:")
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = newName,
                            onValueChange = { 
                                newName = it
                                pdfViewModel.updateNewPdfName(it)
                            },
                            modifier = Modifier.fillMaxWidth(),
                            isError = !pdfViewModel.pdfNameValidation.isValid,
                            supportingText = {
                                if (!pdfViewModel.pdfNameValidation.isValid) {
                                    Text(
                                        text = pdfViewModel.pdfNameValidation.errorMessage,
                                        color = MaterialTheme.colorScheme.error
                                    )
                                } else {
                                    Text("File must end with .pdf extension")
                                }
                            }
                        )
                    }
                } else {
                    Text(text = "Are you sure you want to delete this PDF?")
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (pdfViewModel.pdfToRename != null) {
                            val updatedPdf = pdfViewModel.pdfToRename!!.copy(pdfName = newName)
                            pdfViewModel.updatePdf(updatedPdf)
                        } else if (pdfViewModel.pdfToDelete != null) {
                            pdfViewModel.deletePdf(pdfViewModel.pdfToDelete!!)
                        }
                        pdfViewModel.setShowRenameDeleteDialogState(false)
                    },
                    enabled = pdfViewModel.pdfToDelete != null || pdfViewModel.pdfNameValidation.isValid
                ) {
                    Text(text = if (pdfViewModel.pdfToRename != null) "Rename" else "Delete")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { pdfViewModel.setShowRenameDeleteDialogState(false) }
                ) {
                    Text(text = "Cancel")
                }
            }
        )
    }
} 