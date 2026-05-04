package com.kwh.pdfrederall.ui.screens

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kwh.pdfrederall.data.model.ProcessingState
import com.kwh.pdfrederall.ui.theme.*
import com.kwh.pdfrederall.viewmodel.PdfViewModel
import kotlinx.coroutines.delay
import java.io.File

@Composable
fun SuccessScreen(
    viewModel: PdfViewModel,
    onDownload: () -> Unit,
    onShare: () -> Unit,
    onGoHome: () -> Unit
) {
    val state by viewModel.processingState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val success = state as? ProcessingState.Success
    // Confetti-like particles
    val particles = remember {
        List(12) {
            object {
                val x = (0..100).random()
                val delay = (0..800).random()
                val color = listOf(
                    Color(0xFFFFD700), Color(0xFF2979FF), Color(0xFFE53935),
                    Color(0xFF00C853), Color(0xFFFF6D00), Color(0xFFAA00FF)
                ).random()
            }
        }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "confetti")
    val confettiY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "confetti_y"
    )

    // Scale in animation for the icon
    val iconScale by animateFloatAsState(
        targetValue = 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMediumLow
        ),
        label = "icon_scale"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(NavyDeep),
        contentAlignment = Alignment.Center
    ) {
        // Confetti particles (simplified)
        particles.forEach { particle ->
            Box(
                modifier = Modifier
                    .offset(
                        x = (particle.x * 3.6).dp,
                        y = ((confettiY * 1000 + particle.delay) % 800 - 100).dp
                    )
                    .size(8.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(particle.color.copy(alpha = 0.7f))
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(32.dp)
                .fillMaxWidth()
        ) {
            // Success icon
            Box(
                modifier = Modifier
                    .scale(iconScale)
                    .size(90.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(
                        Brush.radialGradient(
                            colors = listOf(Color(0xFFE53935), Color(0xFFB71C1C))
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.PictureAsPdf,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(52.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                "Success!",
                style = MaterialTheme.typography.displayMedium,
                color = TextPrimary,
                fontWeight = FontWeight.ExtraBold
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Savings badge
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(24.dp))
                    .background(SuccessGreen)
                    .padding(horizontal = 24.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "Saved ${success?.savedPercent ?: 74}% space",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Stats card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceCard)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatRow(
                        label = "Original:",
                        value = fileSizeFormatted(success?.originalSizeBytes ?: 5_360_000),
                        valueColor = TextSecondary
                    )
                    Divider(color = DividerColor, thickness = 0.5.dp)
                    StatRow(
                        label = "Compressed:",
                        value = fileSizeFormatted(success?.resultSizeBytes ?: 1_363_000),
                        valueColor = GreenAccent
                    )
                    Divider(color = DividerColor, thickness = 0.5.dp)
                    StatRow(
                        label = "Saved:",
                        value = fileSizeFormatted((success?.originalSizeBytes ?: 5_360_000) - (success?.resultSizeBytes ?: 1_363_000)),
                        valueColor = BlueAccent
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Action buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = {
                        downloadFile(context, success?.resultUri!!)
                        onDownload()
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp),
                    shape = RoundedCornerShape(25.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = BlueAccent)
                ) {
                    Icon(Icons.Default.Download, contentDescription = null, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Download", fontWeight = FontWeight.Bold)
                }

                OutlinedButton(
                    onClick = {
                        val resultUri = success?.resultUri
                        if (resultUri != null) {
                            shareFile(context, resultUri)
                        }
                        onShare()
                    },
                    modifier = Modifier.weight(1f).height(50.dp),
                    shape = RoundedCornerShape(25.dp),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, BlueAccent),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = BlueAccent)
                ) {
                    Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Share", fontWeight = FontWeight.Bold, color = BlueAccent)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(onClick = onGoHome) {
                Icon(Icons.Default.Home, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Back to Home", color = TextSecondary)
            }
        }
    }
}

@Composable
fun StatRow(label: String, value: String, valueColor: Color) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
        Text(value, style = MaterialTheme.typography.bodyLarge, color = valueColor, fontWeight = FontWeight.Bold)
    }
}

fun downloadFile(context: Context, uri: Uri) {
    try {
        if (uri != null) {
            val resolver = context.contentResolver
            val inputStream = resolver.openInputStream(uri)
                ?: throw Exception("Cannot open input stream")

            val fileName = "pdfreader_${System.currentTimeMillis()}.pdf"

            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
            }

            val fileUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                resolver.insert(
                    MediaStore.Downloads.EXTERNAL_CONTENT_URI,
                    contentValues
                ) ?: throw Exception("Failed to create file")
            } else {
                TODO("VERSION.SDK_INT < Q")
            }

            val outputStream = resolver.openOutputStream(fileUri)
                ?: throw Exception("Cannot open output stream")

            inputStream.copyTo(outputStream)

            inputStream.close()
            outputStream.close()

            Toast.makeText(context, "Saved to Downloads", Toast.LENGTH_SHORT).show()

        }
        else{
            Toast.makeText(context, "Error to Downloads", Toast.LENGTH_SHORT).show()

        }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Download failed", Toast.LENGTH_SHORT).show()
        }
    }

//fixed the code

private fun shareFile(context: Context, uri: Uri) {
    try {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "application/pdf"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        context.startActivity(Intent.createChooser(intent, "Share PDF"))

    } catch (e: Exception) {
        e.printStackTrace()
        Toast.makeText(context, "Unable to share file", Toast.LENGTH_SHORT).show()
    }
}
