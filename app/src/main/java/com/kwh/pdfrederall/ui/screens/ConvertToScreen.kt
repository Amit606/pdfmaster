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
import androidx.compose.ui.unit.sp
import com.kwh.pdfrederall.data.model.ConvertFormat
import com.kwh.pdfrederall.ui.theme.*
import com.kwh.pdfrederall.viewmodel.PdfViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConvertToScreen(
    viewModel: PdfViewModel,
    onBack: () -> Unit,
    onFormatSelected: (ConvertFormat) -> Unit
) {
    Scaffold(
        containerColor = NavyDeep,
        topBar = {
            TopAppBar(
                title = {
                    Text("Convert to...", style = MaterialTheme.typography.titleLarge, color = TextPrimary, fontWeight = FontWeight.Bold)
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
                .padding(padding)
                .padding(16.dp)
        ) {
            Text(
                "Choose output format",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary,
                modifier = Modifier.padding(bottom = 20.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ConvertFormatCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.Description,
                    label = "Word",
                    iconColor = Color(0xFF1565C0),
                    iconBg = Color(0x261565C0),
                    format = ConvertFormat.WORD,
                    onClick = { onFormatSelected(ConvertFormat.WORD) }
                )
                ConvertFormatCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.Image,
                    label = "JPG",
                    iconColor = OrangeAccent,
                    iconBg = IconBgOrange,
                    format = ConvertFormat.JPG,
                    onClick = { onFormatSelected(ConvertFormat.JPG) }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ConvertFormatCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.Landscape,
                    label = "PNG",
                    iconColor = GreenAccent,
                    iconBg = IconBgGreen,
                    format = ConvertFormat.PNG,
                    onClick = { onFormatSelected(ConvertFormat.PNG) }
                )
                ConvertFormatCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.TextSnippet,
                    label = "TXT",
                    iconColor = BlueLight,
                    iconBg = IconBgBlue,
                    format = ConvertFormat.TXT,
                    onClick = { onFormatSelected(ConvertFormat.TXT) }
                )
            }
        }
    }
}

@Composable
fun ConvertFormatCard(
    modifier: Modifier = Modifier,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    iconColor: Color,
    iconBg: Color,
    format: ConvertFormat,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .aspectRatio(1f)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = LocalIndication.current
            ) {
                onClick()
            },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .background(iconBg, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = label, tint = iconColor, modifier = Modifier.size(30.dp))
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                label,
                style = MaterialTheme.typography.labelLarge,
                color = TextPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )
        }
    }
}
