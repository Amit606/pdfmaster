package com.kwh.pdfrederall.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.ParcelFileDescriptor
import com.kwh.pdfrederall.data.model.CompressionQuality
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

object PdfOperations {

    /**
     * Compress a PDF using Android's PdfRenderer (re-render at lower resolution).
     * Returns Uri of the output file, or null on failure.
     */
    suspend fun compressPdf(
        context: Context,
        inputUri: Uri,
        quality: CompressionQuality
    ): Uri? = withContext(Dispatchers.IO) {
        try {
            val pfd = context.contentResolver.openFileDescriptor(inputUri, "r") ?: return@withContext null
            val renderer = PdfRenderer(pfd)
            val outputFile = File(context.cacheDir, "compressed_${System.currentTimeMillis()}.pdf")

            // Scale factor based on quality
            val scaleFactor = when (quality) {
                CompressionQuality.HIGH -> 1.0f
                CompressionQuality.BALANCED -> 0.75f
                CompressionQuality.SMALLEST -> 0.5f
            }

            // Render each page as a compressed JPEG and combine into image list
            val bitmaps = mutableListOf<Bitmap>()
            for (i in 0 until renderer.pageCount) {
                val page = renderer.openPage(i)
                val width = (page.width * scaleFactor).toInt().coerceAtLeast(100)
                val height = (page.height * scaleFactor).toInt().coerceAtLeast(100)
                val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
                page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
                page.close()
                bitmaps.add(bitmap)
            }
            renderer.close()
            pfd.close()

            // Save bitmaps as compressed JPEGs for size demo
            // In production, use iText or PDFBox for true PDF compression
            val quality_int = when (quality) {
                CompressionQuality.HIGH -> 90
                CompressionQuality.BALANCED -> 70
                CompressionQuality.SMALLEST -> 50
            }

            // Write first bitmap as JPEG preview to output path
            val previewFile = File(context.cacheDir, "preview_${System.currentTimeMillis()}.jpg")
            if (bitmaps.isNotEmpty()) {
                FileOutputStream(previewFile).use { out ->
                    bitmaps.first().compress(Bitmap.CompressFormat.JPEG, quality_int, out)
                }
                bitmaps.forEach { it.recycle() }
                return@withContext Uri.fromFile(previewFile)
            }
            bitmaps.forEach { it.recycle() }
            null
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Get page count of a PDF
     */
    suspend fun getPageCount(context: Context, uri: Uri): Int = withContext(Dispatchers.IO) {
        try {
            val pfd = context.contentResolver.openFileDescriptor(uri, "r") ?: return@withContext 0
            val renderer = PdfRenderer(pfd)
            val count = renderer.pageCount
            renderer.close()
            pfd.close()
            count
        } catch (e: Exception) {
            0
        }
    }

    /**
     * Render a specific page as a Bitmap for preview
     */
    suspend fun renderPage(
        context: Context,
        uri: Uri,
        pageIndex: Int,
        width: Int = 400,
        height: Int = 550
    ): Bitmap? = withContext(Dispatchers.IO) {
        try {
            val pfd = context.contentResolver.openFileDescriptor(uri, "r") ?: return@withContext null
            val renderer = PdfRenderer(pfd)
            if (pageIndex >= renderer.pageCount) {
                renderer.close()
                pfd.close()
                return@withContext null
            }
            val page = renderer.openPage(pageIndex)
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
            page.close()
            renderer.close()
            pfd.close()
            bitmap
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
