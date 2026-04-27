package com.kwh.pdfrederall.ui.screens

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kwh.pdfrederall.ui.theme.*
import com.kwh.pdfrederall.viewmodel.PdfViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SplitPdfScreen(
    viewModel: PdfViewModel,
    onBack: () -> Unit,
    onSplitNow: () -> Unit
) {
    var selectedTab by remember { mutableStateOf(0) }
    val rangeText by viewModel.splitRanges.collectAsStateWithLifecycle()
    val selectedPages = remember { mutableStateListOf<Int>() }
    val totalPages = 9

    Scaffold(
        containerColor = NavyDeep,
        topBar = {
            TopAppBar(
                title = {
                    Text("Split PDF", style = MaterialTheme.typography.titleLarge, color = TextPrimary, fontWeight = FontWeight.Bold)
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = TextPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = NavyMedium)
            )
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()   // 👈 IMPORTANT

                    .background(NavyMedium)
                    .padding(16.dp)
            ) {
                if (selectedTab == 1) {
                    Text(
                        "Hint: Use ',' or '-' to define page ranges",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextHint,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
                Button(
                    onClick = onSplitNow,
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RoundedCornerShape(28.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = BlueAccent)
                ) {
                    Icon(Icons.Default.ContentCut, contentDescription = null, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Split Now", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            // Tabs
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(SurfaceCard)
                    .padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                listOf("All Pages", "Range").forEachIndexed { index, label ->
                    val isSelected = selectedTab == index
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (isSelected) BlueAccent else Color.Transparent)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = LocalIndication.current
                            ) {
                                selectedTab = index
                            }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            label,
                            style = MaterialTheme.typography.labelLarge,
                            color = if (isSelected) Color.White else TextSecondary,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (selectedTab == 0) {
                // Page grid
                Text("Select pages to extract:", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                Spacer(modifier = Modifier.height(12.dp))

                val rows = (totalPages + 2) / 3
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    for (row in 0 until rows) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            for (col in 0..2) {
                                val pageNum = row * 3 + col + 1
                                if (pageNum <= totalPages) {
                                    val isSelected = selectedPages.contains(pageNum)
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .aspectRatio(0.75f)
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(if (isSelected) IconBgBlue else SurfaceCard)
                                            .border(
                                                width = if (isSelected) 2.dp else 0.dp,
                                                color = if (isSelected) BlueAccent else Color.Transparent,
                                                shape = RoundedCornerShape(10.dp)
                                            )
                                            .clickable(
                                                interactionSource = remember { MutableInteractionSource() },
                                                indication = LocalIndication.current
                                            ) {
                                                if (isSelected) selectedPages.remove(pageNum)
                                                else selectedPages.add(pageNum)
                                            },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                            Icon(
                                                Icons.Default.Article,
                                                contentDescription = null,
                                                tint = if (isSelected) BlueAccent else TextHint,
                                                modifier = Modifier.size(28.dp)
                                            )
                                            Text(
                                                "$pageNum",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = if (isSelected) BlueAccent else TextSecondary,
                                                fontWeight = FontWeight.SemiBold
                                            )
                                        }
                                    }
                                } else {
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                            }
                        }
                    }
                }
            } else {
                // Range input
                Text("Enter page ranges:", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = rangeText,
                    onValueChange = { viewModel.setSplitRanges(it) },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("e.g. 1-4, 7-9", color = TextHint) },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = BlueAccent,
                        unfocusedBorderColor = DividerColor,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        cursorColor = BlueAccent,
                        focusedContainerColor = SurfaceCard,
                        unfocusedContainerColor = SurfaceCard
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Range chips
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("1 to 4", "7-9").forEach { range ->
                        SuggestionChip(
                            onClick = {
                                val current = if (rangeText.isEmpty()) range else "$rangeText, $range"
                                viewModel.setSplitRanges(current)
                            },
                            label = { Text(range, color = BlueAccent) },
                            colors = SuggestionChipDefaults.suggestionChipColors(containerColor = IconBgBlue),
                            icon = {
                                Icon(Icons.Default.Add, contentDescription = null, tint = BlueAccent, modifier = Modifier.size(16.dp))
                            }
                        )
                    }
                }
            }
        }
    }
}
