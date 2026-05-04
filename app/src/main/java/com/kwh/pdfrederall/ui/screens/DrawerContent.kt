package com.kwh.pdfrederall.ui.screens

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Compress
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MergeType
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kwh.pdfrederall.data.model.PdfOperation
import com.kwh.pdfrederall.navigation.Routes

@Composable
fun DrawerContent(
    onItemClick: (PdfOperation) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .width(280.dp)
            .background(Color.White)
            .padding(16.dp)
    ) {

        Text("PDF Tools", fontSize = 20.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(20.dp))

        DrawerItem("Compress PDF", Icons.Default.Compress) {
            onItemClick(PdfOperation.COMPRESS)
        }
        DrawerItem("Convert PDF", Icons.Default.SwapHoriz) {
            onItemClick(PdfOperation.CONVERT)
        }
        DrawerItem("Merge PDF", Icons.Default.MergeType) {
            onItemClick(PdfOperation.MERGE)
        }
        DrawerItem("Split PDF", Icons.Default.ContentCut) {
            onItemClick(PdfOperation.SPLIT)
        }

        Divider(modifier = Modifier.padding(vertical = 12.dp))

        DrawerSimpleItem("Share", Icons.Default.Share) {}
        DrawerSimpleItem("About Us", Icons.Default.Info) {}
        DrawerSimpleItem("Settings", Icons.Default.Settings) {

        }

        Spacer(modifier = Modifier.weight(1f))

        Text("Version 1.0.0", fontSize = 12.sp, color = Color.Gray)
    }
}
@Composable
fun DrawerItem(
    title: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    NavigationDrawerItem(
        label = { Text(title) },
        selected = false,
        onClick = onClick,
        icon = { Icon(icon, contentDescription = null) },
        modifier = Modifier.padding(vertical = 4.dp)
    )
}

@Composable
fun DrawerSimpleItem(
    title: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    NavigationDrawerItem(
        label = { Text(title) },
        selected = false,
        onClick = onClick,
        icon = { Icon(icon, contentDescription = null) },
        modifier = Modifier.padding(vertical = 4.dp)
    )
}