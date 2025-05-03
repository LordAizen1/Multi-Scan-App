package com.example.multi_scan.data.repository

import com.example.multi_scan.data.database.PdfDao
import com.example.multi_scan.data.models.PdfEntity
import kotlinx.coroutines.flow.Flow

class PdfRepository(private val pdfDao: PdfDao) {
    fun getAllPdf(): Flow<List<PdfEntity>> = pdfDao.getAllPdf()

    suspend fun insertPdf(pdfEntity: PdfEntity) = pdfDao.insertPdf(pdfEntity)

    suspend fun updatePdf(pdfEntity: PdfEntity) = pdfDao.updatePdf(pdfEntity)

    suspend fun deletePdf(pdfEntity: PdfEntity) = pdfDao.deletePdf(pdfEntity)
} 