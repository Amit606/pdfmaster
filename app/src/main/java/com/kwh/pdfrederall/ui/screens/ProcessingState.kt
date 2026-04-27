package com.kwh.pdfrederall.ui.screens

import android.net.Uri

sealed class ProcessingState {
    object Idle : ProcessingState()
    data class Processing(
        val progress: Float,
        val currentPage: Int,
        val totalPages: Int,
        val remainingSeconds: Int,
        val operation: String
    ) : ProcessingState()

    data class Success(
        val resultUri: Uri,
        val originalSizeBytes: Long,
        val resultSizeBytes: Long,
        val savedPercent: Int
    ) : ProcessingState()

    data class Error(val message: String) : ProcessingState()
}