package com.kwh.pdfrederall.ui.screens

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
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
import androidx.compose.ui.unit.dp
import com.kwh.pdfrederall.data.model.CompressionQuality
import com.kwh.pdfrederall.ui.theme.*
import com.kwh.pdfrederall.viewmodel.PdfViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompressOptionsScreen(
    viewModel: PdfViewModel,
    onBack: () -> Unit,
    onQualitySelected: (CompressionQuality) -> Unit
) {
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
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
                .padding(padding)
                .padding(horizontal = 16.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                "Select compression level",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            CompressionOptionCard(
                icon = Icons.Default.Star,
                iconColor = BlueAccent,
                iconBg = IconBgBlue,
                title = "High Quality",
                subtitle = "Minimal size reduction, best quality",
                quality = CompressionQuality.HIGH,
                onClick = { onQualitySelected(CompressionQuality.HIGH) }
            )

            CompressionOptionCard(
                icon = Icons.Default.Balance,
                iconColor = OrangeAccent,
                iconBg = IconBgOrange,
                title = "Balanced",
                subtitle = "Good balance of size and quality",
                quality = CompressionQuality.BALANCED,
                onClick = { onQualitySelected(CompressionQuality.BALANCED) }
            )

            CompressionOptionCard(
                icon = Icons.Default.Compress,
                iconColor = GreenAccent,
                iconBg = IconBgGreen,
                title = "Smallest Size",
                subtitle = "Maximum compression",
                quality = CompressionQuality.SMALLEST,
                onClick = { onQualitySelected(CompressionQuality.SMALLEST) }
            )
        }
    }
}

@Composable
fun CompressionOptionCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconColor: Color,
    iconBg: Color,
    title: String,
    subtitle: String,
    quality: CompressionQuality,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = LocalIndication.current
            ) {
                onClick()
            },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(iconBg, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(26.dp))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.titleMedium, color = TextPrimary, fontWeight = FontWeight.SemiBold)
                Text(subtitle, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
            }
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = TextHint)
        }
    }
}
