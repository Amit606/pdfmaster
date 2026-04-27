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
            .statusBarsPadding()      // 🔝 top safe (toolbar / notch)
            .navigationBarsPadding()
            .background(NavyDeep)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            // 🔥 HEADER (Premium SaaS Style)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(BlueGradientStart, NavyDeep)
                        )
                    )
                    .padding(horizontal = 24.dp, vertical = 32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {

                    Text(
                        text = "PDF Toolkit",
                        style = MaterialTheme.typography.headlineLarge,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "All-in-one PDF tools to compress, convert, and organize your documents effortlessly.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.85f),
                        textAlign = TextAlign.Center,
                        lineHeight = 20.sp
                    )

                    Spacer(modifier = Modifier.height(28.dp))

                    // 🔥 Upload CTA (Improved)
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
                            .fillMaxWidth(0.75f)
                            .height(54.dp),
                        shape = RoundedCornerShape(28.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = BlueAccent
                        ),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Upload,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = "Upload PDF",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 15.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // 🔒 Trust Line (Important for US users)
                    Text(
                        text = "Secure • Private • No signup required",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.7f)
                    )

                    Spacer(modifier = Modifier.height(28.dp))
                }
            }

            // 🔥 TOOL GRID
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .offset(y = (-20).dp)
            ) {

                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    PdfToolCard(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Default.Compress,
                        label = "Compress",
                        iconBg = IconBgBlue,
                        iconColor = BlueAccent,
                        onClick = { onNavigateToSelectFiles(PdfOperation.COMPRESS) }
                    )

                    PdfToolCard(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Default.SwapHoriz,
                        label = "Convert",
                        iconBg = IconBgOrange,
                        iconColor = OrangeAccent,
                        onClick = { onNavigateToSelectFiles(PdfOperation.CONVERT) }
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    PdfToolCard(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Default.MergeType,
                        label = "Merge",
                        iconBg = IconBgGreen,
                        iconColor = GreenAccent,
                        onClick = { onNavigateToSelectFiles(PdfOperation.MERGE) }
                    )

                    PdfToolCard(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Default.ContentCut,
                        label = "Split",
                        iconBg = Color(0x261565C0),
                        iconColor = BlueLight,
                        onClick = { onNavigateToSelectFiles(PdfOperation.SPLIT) }
                    )
                }

                Spacer(modifier = Modifier.height(28.dp))

                // 🔥 FOOTER (Cleaner US Style)
                Text(
                    text = "Your files are processed securely and automatically deleted after use.",
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
        targetValue = if (pressed) 0.96f else 1f,
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
                pressed = true
                onClick()
                pressed = false
            },
        shape = RoundedCornerShape(20.dp), // 🔥 softer, modern corners
        colors = CardDefaults.cardColors(
            containerColor = NavyLight
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp, // 🔥 slightly premium depth
            pressedElevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 18.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            // 🔥 Icon Container (modern SaaS style)
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(iconBg.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = iconColor,
                    modifier = Modifier.size(26.dp)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = TextPrimary,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Medium, // 🔥 less heavy = more modern
                fontSize = 14.sp
            )
        }
    }
}
