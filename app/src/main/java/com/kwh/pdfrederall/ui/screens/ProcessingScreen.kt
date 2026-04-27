package com.kwh.pdfrederall.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kwh.pdfrederall.data.model.ProcessingState
import com.kwh.pdfrederall.ui.theme.*
import com.kwh.pdfrederall.viewmodel.PdfViewModel

@Composable
fun ProcessingScreen(
    viewModel: PdfViewModel,
    onComplete: () -> Unit
) {
    val state by viewModel.processingState.collectAsStateWithLifecycle()

    // Navigate to success when done
    LaunchedEffect(state) {
        if (state is ProcessingState.Success || state is ProcessingState.Error) {
            onComplete()
        }
    }

    val processing = state as? ProcessingState.Processing

    // Bouncing animation for PDF icon
    val infiniteTransition = rememberInfiniteTransition(label = "bounce")
    val bounce1 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -24f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse,
            initialStartOffset = StartOffset(0)
        ),
        label = "bounce1"
    )
    val bounce2 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -24f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse,
            initialStartOffset = StartOffset(200)
        ),
        label = "bounce2"
    )
    val scale1 by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(600),
            repeatMode = RepeatMode.Reverse,
            initialStartOffset = StartOffset(0)
        ),
        label = "scale1"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(NavyDeep),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(32.dp)
        ) {
            // Bouncing PDF icons
            Row(
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                verticalAlignment = Alignment.Bottom,
                modifier = Modifier.height(120.dp)
            ) {
                // Icon 1
                Box(
                    modifier = Modifier
                        .offset(y = bounce1.dp)
                        .scale(scale1)
                        .size(70.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color(0xFFE53935), Color(0xFFB71C1C))
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.PictureAsPdf,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(42.dp)
                    )
                }

                // Icon 2
                Box(
                    modifier = Modifier
                        .offset(y = bounce2.dp)
                        .size(70.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color(0xFFE53935), Color(0xFFB71C1C))
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.PictureAsPdf,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(42.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            Text(
                "Processing...",
                style = MaterialTheme.typography.headlineMedium,
                color = TextPrimary,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = if (processing != null)
                    "${processing.operation} page ${processing.currentPage} of ${processing.totalPages}..."
                else "Preparing...",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Progress bar
            LinearProgressIndicator(
                progress = processing?.progress ?: 0f,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = BlueAccent,
                trackColor = ProgressBg
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = if (processing != null) {
                    val mins = processing.remainingSeconds / 60
                    val secs = processing.remainingSeconds % 60
                    String.format("%02d:%02d Remaining", mins, secs)
                } else "Calculating...",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
            )
        }
    }
}
