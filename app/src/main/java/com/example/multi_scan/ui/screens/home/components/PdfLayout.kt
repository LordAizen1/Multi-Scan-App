package com.example.multi_scan.ui.screens.home.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.multi_scan.data.models.PdfEntity

@Composable
fun PdfLayout(
    pdfEntity: PdfEntity,
    onDelete: (PdfEntity) -> Unit,
    onRename: (PdfEntity) -> Unit,
    onView: (PdfEntity) -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onView(pdfEntity) }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = pdfEntity.pdfName,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Size: ${pdfEntity.pdfSize}"
                )
            }
            
            IconButton(onClick = { onRename(pdfEntity) }) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = "Rename PDF")
            }
            
            Spacer(modifier = Modifier.width(8.dp))
            
            IconButton(onClick = { onDelete(pdfEntity) }) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete PDF")
            }
        }
    }
} 