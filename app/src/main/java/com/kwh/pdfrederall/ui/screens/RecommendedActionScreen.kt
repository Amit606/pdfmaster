package com.kwh.pdfrederall.ui.screens

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kwh.pdfrederall.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecommendedActionScreen(
    viewModel: com.kwh.pdfrederall.viewmodel.PdfViewModel,
    onBack: () -> Unit,
    onCompress: () -> Unit,
    onConvert: () -> Unit,
    onMerge: () -> Unit
) {
    Scaffold(
        containerColor = NavyDeep,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Recommended",
                        style = MaterialTheme.typography.titleLarge,
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = null,
                            tint = TextPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = NavyMedium
                )
            )
        }
    ){ padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Recommended Action",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Compress PDF",
                style = MaterialTheme.typography.displayMedium,
                color = TextPrimary,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Estimated 72% savings",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(36.dp))

            // Primary action button
            Button(
                onClick = onCompress,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BlueAccent)
            ) {
                Icon(Icons.Default.Compress, contentDescription = null, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Compress PDF",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Secondary options
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceCard)
            ) {
                Column {
                    ActionOptionRow(
                        icon = Icons.Default.Description,
                        iconColor = BlueAccent,
                        iconBg = IconBgBlue,
                        label = "Convert to Word",
                        onClick = onConvert
                    )
                    Divider(color = DividerColor, thickness = 1.dp, modifier = Modifier.padding(horizontal = 16.dp))
                    ActionOptionRow(
                        icon = Icons.Default.MergeType,
                        iconColor = GreenAccent,
                        iconBg = IconBgGreen,
                        label = "Merge Files",
                        onClick = onMerge
                    )
                }
            }
        }
    }
}

@Composable
fun ActionOptionRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconColor: Color,
    iconBg: Color,
    label: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = LocalIndication.current
            ) {
                onClick()
            }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(iconBg, RoundedCornerShape(10.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(22.dp))
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = TextPrimary,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1f)
        )
        Icon(
            Icons.Default.ChevronRight,
            contentDescription = null,
            tint = TextHint,
            modifier = Modifier.size(22.dp)
        )
    }
}
