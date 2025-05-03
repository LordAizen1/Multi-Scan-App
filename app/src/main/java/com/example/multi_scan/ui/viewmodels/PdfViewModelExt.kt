package com.example.multi_scan.ui.viewmodels

/**
 * Extension functions for PdfViewModel to resolve platform declaration clashes
 */

/**
 * Function to handle setting showDialog state in the ViewModel
 */
fun PdfViewModel.setShowDialogState(show: Boolean) {
    this.updateShowDialogState(show)
}

/**
 * Function to handle setting showRenameDeleteDialog state in the ViewModel
 */
fun PdfViewModel.setShowRenameDeleteDialogState(show: Boolean) {
    this.updateRenameDeleteDialogState(show)
} 