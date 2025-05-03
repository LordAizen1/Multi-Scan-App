package com.example.multi_scan.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pdf_table")
data class PdfEntity(
    @PrimaryKey
    val id: String,
    val pdfName: String,
    val pdfSize: String
) 