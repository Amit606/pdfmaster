package com.kwh.pdfrederall.ui.screens

import android.app.Activity
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kwh.pdfrederall.data.model.PdfOperation
import com.kwh.pdfrederall.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToSelectFiles: (PdfOperation) -> Unit
) {
    val scrollState = rememberScrollState()

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(NavyDeep)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(46.dp))

                // Header with gradient background
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(BlueGradientStart, NavyDeep),
                                startY = 0f,
                                endY = 600f
                            )
                        )
                        .padding(horizontal = 24.dp, vertical = 32.dp),
                    contentAlignment = Alignment.TopCenter
                ) {

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        // Top bar
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,   // 👈 change this
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Text(
                                text = "PDF Tools",
                                style = MaterialTheme.typography.headlineLarge,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )

                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Compress, Convert,\nMerge & Split PDFs",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White.copy(alpha = 0.9f),
                            textAlign = TextAlign.Center,
                            lineHeight = 24.sp
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        // Upload Files Button
                        val launcher = rememberLauncherForActivityResult(
                            contract = ActivityResultContracts.OpenMultipleDocuments()
                        ) { uris ->
                            if (uris.isNotEmpty()) {
                                onNavigateToSelectFiles(PdfOperation.COMPRESS)
                            }
                        }

                        Button(
                            onClick = { launcher.launch(arrayOf("application/pdf")) },
                            modifier = Modifier
                                .fillMaxWidth(0.7f)
                                .height(52.dp),
                            shape = RoundedCornerShape(28.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White,
                                contentColor = BlueAccent
                            ),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Upload,
                                contentDescription = null,
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Upload Files",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Compress, Convert, Merge & Split PDFs",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.7f)
                        )

                        Spacer(modifier = Modifier.height(32.dp))
                    }
                }

                // Tool Cards Grid
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .offset(y = (-20).dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        PdfToolCard(
                            modifier = Modifier.weight(1f),
                            icon = Icons.Default.Compress,
                            label = "Compress PDF",
                            iconBg = IconBgBlue,
                            iconColor = BlueAccent,
                            onClick = { onNavigateToSelectFiles(PdfOperation.COMPRESS) }
                        )
                        PdfToolCard(
                            modifier = Modifier.weight(1f),
                            icon = Icons.Default.SwapHoriz,
                            label = "Convert PDF",
                            iconBg = IconBgOrange,
                            iconColor = OrangeAccent,
                            onClick = { onNavigateToSelectFiles(PdfOperation.CONVERT) }
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        PdfToolCard(
                            modifier = Modifier.weight(1f),
                            icon = Icons.Default.MergeType,
                            label = "Merge PDF",
                            iconBg = IconBgGreen,
                            iconColor = GreenAccent,
                            onClick = { onNavigateToSelectFiles(PdfOperation.MERGE) }
                        )
                        PdfToolCard(
                            modifier = Modifier.weight(1f),
                            icon = Icons.Default.ContentCut,
                            label = "Split PDF",
                            iconBg = Color(0x261565C0),
                            iconColor = BlueLight,
                            onClick = { onNavigateToSelectFiles(PdfOperation.SPLIT) }
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Footer
                    Text(
                        text = "🔒 Secure · No signup required · Files deleted after processing",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextHint,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }


@Composable
fun PdfToolCard(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    label: String,
    iconBg: Color,
    iconColor: Color,
    onClick: () -> Unit
) {
    var pressed by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.97f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
        label = "card_scale"
    )

    Card(
        modifier = modifier
            .aspectRatio(1f)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = LocalIndication.current
            ) {
                onClick()
            },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = NavyLight),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(iconBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = iconColor,
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = TextPrimary,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.SemiBold,
                fontSize = 13.sp
            )
        }
    }
}
