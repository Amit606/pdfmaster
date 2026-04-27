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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kwh.pdfrederall.data.model.CompressionQuality
import com.kwh.pdfrederall.ui.theme.*
import com.kwh.pdfrederall.viewmodel.PdfViewModel
@Composable
fun IntroScreen(
    onGetStarted: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(NavyDeep),
        contentAlignment = Alignment.Center
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            Spacer(modifier = Modifier.height(20.dp))

            // 🔹 App Logo / Icon
            Icon(
                imageVector = Icons.Default.PictureAsPdf,
                contentDescription = null,
                tint = BlueAccent,
                modifier = Modifier.size(100.dp)
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "PDF Master Tools",
                    style = MaterialTheme.typography.headlineMedium,
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    "Compress, Convert, Merge & Split PDFs easily",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary,
                    textAlign = TextAlign.Center
                )
            }

            // 🔹 Features
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                IntroFeature("Compress PDF", Icons.Default.Compress)
                IntroFeature("Convert Files", Icons.Default.SwapHoriz)
                IntroFeature("Merge & Split", Icons.Default.MergeType)
            }

            // 🔹 Button
            Button(
                onClick = onGetStarted,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(26.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BlueAccent)
            ) {
                Text("Get Started", fontWeight = FontWeight.Bold)
            }
        }
    }
}
@Composable
fun IntroFeature(text: String, icon: ImageVector) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = BlueAccent)
        Spacer(modifier = Modifier.width(10.dp))
        Text(text, color = TextPrimary)
    }
}