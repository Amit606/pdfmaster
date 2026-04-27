package com.kwh.pdfrederall.data.model

/**
 * Represents a PDF file selected by the user
 */
data class PdfFile(
    val uri: android.net.Uri,
    val name: String,
    val sizeBytes: Long,
    val path: String = ""
) {
    val sizeFormatted: String
        get() {
            val mb = sizeBytes / (1024.0 * 1024.0)
            return if (mb >= 1.0) String.format("%.1f MB", mb)
            else String.format("%.0f KB", sizeBytes / 1024.0)
        }
}

/**
 * PDF operation types
 */
enum class PdfOperation {
    COMPRESS, CONVERT, MERGE, SPLIT
}

/**
 * Quality level for compression
 */
enum class CompressionQuality(val label: String, val description: String) {
    HIGH("High Quality", "Minimal size reduction, best quality"),
    BALANCED("Balanced", "Good balance of size and quality"),
    SMALLEST("Smallest Size", "Maximum compression")
}

/**
 * Conversion output format
 */
enum class ConvertFormat(val label: String, val extension: String) {
    WORD("Word", "docx"),
    JPG("JPG", "jpg"),
    PNG("PNG", "png"),
    TXT("TXT", "txt")
}

/**
 * Processing state
 */
sealed class ProcessingState {
    object Idle : ProcessingState()
    data class Processing(
        val currentPage: Int = 0,
        val totalPages: Int = 0,
        val progress: Float = 0f,
        val remainingSeconds: Int = 0,
        val operation: String = ""
    ) : ProcessingState()
    data class Success(
        val originalSizeBytes: Long = 0L,
        val resultSizeBytes: Long = 0L,
        val savedPercent: Int = 0,
        val resultUri: android.net.Uri? = null
    ) : ProcessingState()
    data class Error(val message: String) : ProcessingState()
}
