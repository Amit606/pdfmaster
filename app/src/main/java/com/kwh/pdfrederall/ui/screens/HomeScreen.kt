package com.kwh.pdfrederall.ui.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kwh.pdfrederall.data.model.PdfOperation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToSelectFiles: (PdfOperation) -> Unit
) {

    val drawerState = rememberDrawerState(DrawerValue.Closed)

    // 👉 Controls
    var openDrawer by remember { mutableStateOf(false) }
    var selectedOperation by remember { mutableStateOf<PdfOperation?>(null) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenMultipleDocuments()
    ) { uris ->
        if (uris.isNotEmpty()) {
            onNavigateToSelectFiles(PdfOperation.COMPRESS)
        }
    }

    // ✅ OPEN DRAWER (SAFE)
    LaunchedEffect(openDrawer) {
        if (openDrawer) {
            drawerState.open()
            openDrawer = false
        }
    }

    // ✅ CLOSE + NAVIGATE (CRASH FIX 🔥)
    LaunchedEffect(selectedOperation) {
        selectedOperation?.let { operation ->

            drawerState.close()

            kotlinx.coroutines.delay(250) // IMPORTANT

            onNavigateToSelectFiles(operation)

            selectedOperation = null
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(
                onItemClick = { operation ->
                    selectedOperation = operation
                }
            )
        }
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF4F6F8))
        ) {

            // 🔵 HEADER
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
                    .background(
                        Brush.verticalGradient(
                            listOf(Color(0xFF1E5EFF), Color(0xFF4A90E2))
                        )
                    )
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 40.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    // ☰ MENU
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        Icon(
                            Icons.Default.Menu,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = LocalIndication.current
                            ) {
                                openDrawer = true
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        "PDF Tools",
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        "Compress, Convert,\nMerge & Split PDFs",
                        color = Color.White.copy(alpha = 0.9f),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = { launcher.launch(arrayOf("application/pdf")) },
                        shape = RoundedCornerShape(30.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF2D7CFF)
                        )
                    ) {
                        Icon(Icons.Default.Upload, contentDescription = null, tint = Color.White)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Upload Files", color = Color.White)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "Compress, Convert, Merge & Split PDFs",
                modifier = Modifier.align(Alignment.CenterHorizontally),
                color = Color.Gray,
                fontSize = 13.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Column(modifier = Modifier.padding(horizontal = 16.dp)) {

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {

                    ToolCard(
                        modifier = Modifier.weight(1f),
                        title = "Compress PDF",
                        color = Color(0xFFFF7043),
                        icon = Icons.Default.Compress
                    ) { onNavigateToSelectFiles(PdfOperation.COMPRESS) }

                    ToolCard(
                        modifier = Modifier.weight(1f),
                        title = "Convert PDF",
                        color = Color(0xFF7E57C2),
                        icon = Icons.Default.SwapHoriz
                    ) { onNavigateToSelectFiles(PdfOperation.CONVERT) }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {

                    ToolCard(
                        modifier = Modifier.weight(1f),
                        title = "Merge PDF",
                        color = Color(0xFF4CAF50),
                        icon = Icons.Default.MergeType
                    ) { onNavigateToSelectFiles(PdfOperation.MERGE) }

                    ToolCard(
                        modifier = Modifier.weight(1f),
                        title = "Split PDF",
                        color = Color(0xFF29B6F6),
                        icon = Icons.Default.ContentCut
                    ) { onNavigateToSelectFiles(PdfOperation.SPLIT) }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                "Secure • No signup required • Files deleted after processing",
                modifier = Modifier.align(Alignment.CenterHorizontally),
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun ToolCard(
    modifier: Modifier,
    title: String,
    color: Color,
    icon: ImageVector,
    onClick: () -> Unit
) {

    Card(
        modifier = modifier
            .height(120.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = LocalIndication.current
            ) { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {

        Column(
            modifier = Modifier.fillMaxSize().padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(color.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = color)
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = title,
                color=Color.Black,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}