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
    suspend fun compressPdfReal(
        context: Context,
        inputUri: Uri,
        quality: CompressionQuality
    ): Uri? = withContext(Dispatchers.IO) {
        try {
            val inputStream = context.contentResolver.openInputStream(inputUri) ?: return@withContext null

            val document = com.tom_roush.pdfbox.pdmodel.PDDocument.load(inputStream)

            val jpegQuality = when (quality) {
                CompressionQuality.HIGH -> 0.9f
                CompressionQuality.BALANCED -> 0.7f
                CompressionQuality.SMALLEST -> 0.5f
            }

            val renderer = com.tom_roush.pdfbox.rendering.PDFRenderer(document)

            val newDoc = com.tom_roush.pdfbox.pdmodel.PDDocument()

            for (i in 0 until document.numberOfPages) {
                val image = renderer.renderImageWithDPI(i, 150f)

                val page = com.tom_roush.pdfbox.pdmodel.PDPage(
                    com.tom_roush.pdfbox.pdmodel.common.PDRectangle(
                        image.width.toFloat(),
                        image.height.toFloat()
                    )
                )
                newDoc.addPage(page)

                val pdImage = com.tom_roush.pdfbox.pdmodel.graphics.image.JPEGFactory.createFromImage(
                    newDoc,
                    image,
                    jpegQuality
                )

                val contentStream = com.tom_roush.pdfbox.pdmodel.PDPageContentStream(newDoc, page)
                contentStream.drawImage(pdImage, 0f, 0f)
                contentStream.close()
            }

            val outputFile = File(context.cacheDir, "compressed_${System.currentTimeMillis()}.pdf")
            newDoc.save(outputFile)

            document.close()
            newDoc.close()

            Uri.fromFile(outputFile)

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
