package com.kwh.pdfrederall.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kwh.pdfrederall.data.model.CompressionQuality
import com.kwh.pdfrederall.ui.theme.*
import com.kwh.pdfrederall.viewmodel.PdfViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompressPreviewScreen(
    viewModel: PdfViewModel,
    onBack: () -> Unit,
    onCompressNow: () -> Unit
) {
    val quality by viewModel.compressionQuality.collectAsStateWithLifecycle()
    val files by viewModel.selectedFiles.collectAsStateWithLifecycle()

    val reductionPercent = when (quality) {
        CompressionQuality.HIGH -> 30
        CompressionQuality.BALANCED -> 55
        CompressionQuality.SMALLEST -> 74
    }

    val estimatedSize = if (files.isNotEmpty()) {
        val original = files.first().sizeBytes
        ((original * (1 - reductionPercent / 100.0)) / 1024).toLong()
    } else 144L

    val animatedProgress by animateFloatAsState(
        targetValue = reductionPercent / 100f,
        animationSpec = tween(1200, easing = FastOutSlowInEasing),
        label = "reduction_progress"
    )

    Scaffold(
        containerColor = NavyDeep,
        topBar = {
            TopAppBar(
                title = {
                    Text("Compress PDF", style = MaterialTheme.typography.titleLarge, color = TextPrimary, fontWeight = FontWeight.Bold)
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = TextPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = NavyMedium)
            )
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .background(NavyMedium)
                    .padding(16.dp)
            ) {
                Button(
                    onClick = onCompressNow,
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RoundedCornerShape(28.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = BlueAccent)
                ) {
                    Icon(Icons.Default.Compress, contentDescription = null, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Compress Now", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // PDF Preview Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceCard)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Default.PictureAsPdf,
                            contentDescription = null,
                            tint = OrangeAccent,
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = files.firstOrNull()?.name ?: "document.pdf",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Size info
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${estimatedSize} KB",
                    style = MaterialTheme.typography.headlineMedium,
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Estimated ${reductionPercent}% Reduction",
                    style = MaterialTheme.typography.bodySmall,
                    color = GreenAccent,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Output Preview", style = MaterialTheme.typography.bodySmall, color = TextHint)
                Text("Page 1 – ${estimatedSize} KB", style = MaterialTheme.typography.bodySmall, color = TextHint)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Reduction badge + progress
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Circular badge
                Box(
                    modifier = Modifier
                        .size(90.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                colors = listOf(BlueGradientStart, BlueDark)
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "$reductionPercent%",
                            style = MaterialTheme.typography.headlineMedium,
                            color = Color.White,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Text(
                            text = "Saved",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "Task Remaining",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    LinearProgressIndicator(
                        progress = animatedProgress,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = BlueAccent,
                        trackColor = ProgressBg
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "${(animatedProgress * 100).toInt()}% estimated reduction",
                        style = MaterialTheme.typography.bodySmall,
                        color = BlueAccent,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Quality mode chip
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                SuggestionChip(
                    onClick = {},
                    label = { Text("Mode: ${quality.label}", color = BlueAccent, fontWeight = FontWeight.SemiBold) },
                    colors = SuggestionChipDefaults.suggestionChipColors(containerColor = IconBgBlue)
                )
            }
        }
    }
}
