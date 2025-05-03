package com.example.multi_scan.ui.viewmodels

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.multi_scan.data.database.PdfDatabase
import com.example.multi_scan.data.models.PdfEntity
import com.example.multi_scan.data.repository.PdfRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File

data class PdfState(
    val pdfList: List<PdfEntity> = emptyList(),
    val isLoading: Boolean = false,
    val error: String = ""
)

data class ValidationState(
    val isValid: Boolean = true,
    val errorMessage: String = ""
)

class PdfViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: PdfRepository

    var isSplashScreen by mutableStateOf(true)
        private set
    
    var showDialog by mutableStateOf(false)
        private set
    
    var showRenameDeleteDialog by mutableStateOf(false)
        private set
    
    var pdfToRename: PdfEntity? by mutableStateOf(null)
        private set
    
    var pdfToDelete: PdfEntity? by mutableStateOf(null)
        private set
    
    var newPdfName by mutableStateOf("")
        private set
        
    var pdfNameValidation by mutableStateOf(ValidationState())
        private set

    private val _pdfStateFlow = MutableStateFlow(PdfState())
    val pdfStateFlow: StateFlow<PdfState> = _pdfStateFlow.asStateFlow()

    init {
        repository = PdfRepository(PdfDatabase.getDatabase(application).pdfDao())
        getAllPdf()
        viewModelScope.launch {
            isSplashScreen = false
        }
    }

    fun getAllPdf() {
        viewModelScope.launch {
            _pdfStateFlow.update { it.copy(isLoading = true) }
            try {
                repository.getAllPdf().collect { pdfList ->
                    _pdfStateFlow.update {
                        it.copy(
                            pdfList = pdfList,
                            isLoading = false,
                            error = ""
                        )
                    }
                }
            } catch (e: Exception) {
                _pdfStateFlow.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "An unexpected error occurred"
                    )
                }
            }
        }
    }

    fun insertPdf(pdfEntity: PdfEntity) {
        viewModelScope.launch {
            try {
                repository.insertPdf(pdfEntity)
            } catch (e: Exception) {
                _pdfStateFlow.update {
                    it.copy(
                        error = e.message ?: "An unexpected error occurred"
                    )
                }
            }
        }
    }

    fun updatePdf(pdfEntity: PdfEntity) {
        viewModelScope.launch {
            try {
                // Validate the file name first
                if (validatePdfName(pdfEntity.pdfName)) {
                    // Rename the actual file on the filesystem
                    val filesDir = getApplication<Application>().filesDir
                    val oldFile = File(filesDir, pdfToRename?.pdfName ?: "")
                    val newFile = File(filesDir, pdfEntity.pdfName)
                    
                    if (oldFile.exists()) {
                        val success = oldFile.renameTo(newFile)
                        if (success) {
                            // Update the database entry only if file rename succeeded
                            repository.updatePdf(pdfEntity)
                        }
                    } else {
                        // Update database anyway if old file doesn't exist
                        repository.updatePdf(pdfEntity)
                    }
                }
            } catch (e: Exception) {
                _pdfStateFlow.update {
                    it.copy(
                        error = e.message ?: "An unexpected error occurred"
                    )
                }
            }
        }
    }

    fun deletePdf(pdfEntity: PdfEntity) {
        viewModelScope.launch {
            try {
                repository.deletePdf(pdfEntity)
            } catch (e: Exception) {
                _pdfStateFlow.update {
                    it.copy(
                        error = e.message ?: "An unexpected error occurred"
                    )
                }
            }
        }
    }

    fun updateShowDialogState(show: Boolean) {
        showDialog = show
    }

    fun updateRenameDeleteDialogState(show: Boolean) {
        showRenameDeleteDialog = show
        // Reset validation state when dialog closes
        if (!show) {
            pdfNameValidation = ValidationState()
        }
    }

    fun setRenamePdf(pdfEntity: PdfEntity) {
        pdfToRename = pdfEntity
        newPdfName = pdfEntity.pdfName
        pdfToDelete = null
        showRenameDeleteDialog = true
    }

    fun setDeletePdf(pdfEntity: PdfEntity) {
        pdfToDelete = pdfEntity
        pdfToRename = null
        showRenameDeleteDialog = true
    }

    fun updateNewPdfName(name: String) {
        newPdfName = name
        // Validate as user types
        validatePdfName(name)
    }
    
    /**
     * Validates that the PDF name has a .pdf extension
     * Returns true if valid, false otherwise
     */
    fun validatePdfName(name: String): Boolean {
        return when {
            name.isBlank() -> {
                pdfNameValidation = ValidationState(
                    isValid = false,
                    errorMessage = "PDF name cannot be empty"
                )
                false
            }
            !name.lowercase().endsWith(".pdf") -> {
                pdfNameValidation = ValidationState(
                    isValid = false,
                    errorMessage = "PDF name must end with .pdf extension"
                )
                false
            }
            name.length <= 4 -> { // Just ".pdf" is not enough
                pdfNameValidation = ValidationState(
                    isValid = false,
                    errorMessage = "PDF name must contain more than just the extension"
                )
                false
            }
            else -> {
                pdfNameValidation = ValidationState(isValid = true)
                true
            }
        }
    }
} 