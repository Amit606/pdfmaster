package com.kwh.pdfrederall.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kwh.pdfrederall.data.model.PdfFile
import com.kwh.pdfrederall.data.model.PdfOperation
import com.kwh.pdfrederall.ui.theme.*
import com.kwh.pdfrederall.viewmodel.PdfViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectFilesScreen(
    viewModel: PdfViewModel,
    operation: PdfOperation,
    onBack: () -> Unit,
    onNext: () -> Unit
) {
    val selectedFiles by viewModel.selectedFiles.collectAsStateWithLifecycle()

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenMultipleDocuments()
    ) { uris ->
        if (uris.isNotEmpty()) {
            viewModel.addFiles(uris)
        }
    }

    val operationLabel = when (operation) {
        PdfOperation.COMPRESS -> "Compress"
        PdfOperation.CONVERT -> "Convert"
        PdfOperation.MERGE -> "Merge"
        PdfOperation.SPLIT -> "Split"
    }

    Scaffold(
        containerColor = NavyDeep,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Select Files",
                        style = MaterialTheme.typography.titleLarge,
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = TextPrimary)
                    }
                },
                actions = {

                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = NavyMedium
                )
            )
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()   // 👈 IMPORTANT

                    .background(NavyMedium)
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                if (selectedFiles.isNotEmpty()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "Total Size:",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondary
                        )
                        Text(
                            fileSizeFormatted(viewModel.totalSelectedSize),
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextPrimary,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = { launcher.launch(arrayOf("application/pdf")) },
                        modifier = Modifier.weight(1f).height(48.dp),
                        shape = RoundedCornerShape(24.dp),
                        border = BorderStroke(1.5.dp, BlueAccent),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = BlueAccent)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Add More", fontWeight = FontWeight.SemiBold)
                    }

                    Button(
                        onClick = onNext,
                        enabled = selectedFiles.isNotEmpty(),
                        modifier = Modifier.weight(1f).height(48.dp),
                        shape = RoundedCornerShape(24.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = BlueAccent,
                            disabledContainerColor = DividerColor
                        )
                    ) {
                        Text(
                            text = if (operation == PdfOperation.MERGE) "Merge Now" else "Next",
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(Icons.Default.ArrowForward, contentDescription = null, modifier = Modifier.size(18.dp))
                    }
                }
            }
        }
    ) { padding ->
        if (selectedFiles.isEmpty()) {
            // Empty state
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(IconBgBlue),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.PictureAsPdf,
                            contentDescription = null,
                            tint = BlueAccent,
                            modifier = Modifier.size(42.dp)
                        )
                    }
                    Text(
                        "No Files Selected",
                        style = MaterialTheme.typography.titleMedium,
                        color = TextPrimary
                    )
                    Text(
                        "Tap below to select PDF files\nto $operationLabel",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                    Button(
                        onClick = { launcher.launch(arrayOf("application/pdf")) },
                        shape = RoundedCornerShape(24.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = BlueAccent)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Select PDF Files", fontWeight = FontWeight.Bold)
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(vertical = 12.dp)
            ) {
                items(selectedFiles) { file ->
                    PdfFileItem(
                        file = file,
                        onRemove = { viewModel.removeFile(file) }
                    )
                }
            }
        }
    }
}

@Composable
fun PdfFileItem(
    file: PdfFile,
    onRemove: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // PDF Icon
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(IconBgOrange),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.PictureAsPdf,
                    contentDescription = null,
                    tint = OrangeAccent,
                    modifier = Modifier.size(26.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = file.name,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextPrimary,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = file.sizeFormatted,
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )
            }

            IconButton(onClick = onRemove) {
                Icon(
                    Icons.Default.DeleteOutline,
                    contentDescription = "Remove",
                    tint = RedAccent
                )
            }
        }
    }
}

fun fileSizeFormatted(bytes: Long): String {
    val mb = bytes / (1024.0 * 1024.0)
    return if (mb >= 1.0) String.format("%.1f MB", mb)
    else String.format("%.0f KB", bytes / 1024.0)
}
