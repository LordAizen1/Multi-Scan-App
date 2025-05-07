package com.example.multi_scan.ui.screens.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.multi_scan.R
import com.example.multi_scan.utils.LanguageUtils

@Composable
fun LanguageSelectionDialog(
    onDismiss: () -> Unit,
    onLanguageSelected: (String) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { 
            Text(
                text = stringResource(id = R.string.change_language),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        },
        text = {
            Column {
                // English option
                Text(
                    text = "English",
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onLanguageSelected(LanguageUtils.ENGLISH) }
                        .padding(vertical = 12.dp),
                    fontSize = 16.sp
                )
                
                Divider()
                
                // Bengali option
                Text(
                    text = "বাংলা (Bengali)",
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onLanguageSelected(LanguageUtils.BENGALI) }
                        .padding(vertical = 12.dp),
                    fontSize = 16.sp
                )
                
                Divider()
                
                // Hindi option
                Text(
                    text = "हिंदी (Hindi)",
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onLanguageSelected(LanguageUtils.HINDI) }
                        .padding(vertical = 12.dp),
                    fontSize = 16.sp
                )
                
                Divider()
                
                // Urdu option
                Text(
                    text = "اردو (Urdu)",
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onLanguageSelected(LanguageUtils.URDU) }
                        .padding(vertical = 12.dp),
                    fontSize = 16.sp
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(id = R.string.cancel))
            }
        }
    )
} 