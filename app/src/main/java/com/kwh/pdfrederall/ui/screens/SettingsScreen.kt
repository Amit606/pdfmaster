package com.kwh.pdfrederall.ui.screens
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.animation.core.*
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
import com.kwh.pdfrederall.ui.theme.*
import kotlinx.coroutines.launch
@Composable
fun SettingsScreen(

    onBack: () -> Unit
) {


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF4F6F8))
    ) {

        // 🔵 TOP BAR
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.ArrowBack,
                contentDescription = null,
                modifier = Modifier.clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = LocalIndication.current
                )
                { onBack() }
            )

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                "Settings",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // ⚙️ SETTINGS LIST
        Column(
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {

            SettingsItem(
                title = "Language",
                subtitle = "e.g. English, Spanish",
                icon = Icons.Default.Language
            ) {
                // 👉 navigate to language screen
            }

            SettingsItem(
                title = "Dark Mode",
                subtitle = "Coming Soon",
                icon = Icons.Default.DarkMode
            ) { }

            SettingsItem(
                title = "Share App",
                subtitle = "",
                icon = Icons.Default.Share
            ) { }

            SettingsItem(
                title = "About Us",
                subtitle = "",
                icon = Icons.Default.Info
            ) { }

            Spacer(modifier = Modifier.height(30.dp))

            Text(
                text = "App Version 1.0.0",
                color = Color.Gray,
                fontSize = 12.sp
            )
        }
    }
}
@Composable
fun SettingsItem(
    title: String,
    subtitle: String,
    icon: ImageVector,
    onClick: () -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = LocalIndication.current
            ) { onClick() },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {

        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(icon, contentDescription = null, tint = Color(0xFF2D7CFF))

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontWeight = FontWeight.Medium)

                if (subtitle.isNotEmpty()) {
                    Text(
                        subtitle,
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }

            Icon(Icons.Default.ArrowForwardIos, contentDescription = null, tint = Color.Gray)
        }
    }
}