package com.example.multi_scan.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.multi_scan.data.models.PdfEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PdfDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPdf(pdfEntity: PdfEntity)

    @Update
    suspend fun updatePdf(pdfEntity: PdfEntity)

    @Delete
    suspend fun deletePdf(pdfEntity: PdfEntity)

    @Query("SELECT * FROM pdf_table ORDER BY pdfName ASC")
    fun getAllPdf(): Flow<List<PdfEntity>>
} 