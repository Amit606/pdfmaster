package com.kwh.pdfrederall.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Compress
import androidx.compose.material.icons.filled.MergeType
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kwh.pdfrederall.data.model.OnboardingPage
import com.kwh.pdfrederall.ui.theme.BlueAccent
import com.kwh.pdfrederall.ui.theme.NavyDeep
import com.kwh.pdfrederall.ui.theme.TextHint
import com.kwh.pdfrederall.ui.theme.TextPrimary
import com.kwh.pdfrederall.ui.theme.TextSecondary

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(
    onFinish: () -> Unit
) {
    val pages = listOf(
        OnboardingPage(
            "Compress PDF",
            "Reduce file size without losing quality",
            Icons.Default.Compress
        ),
        OnboardingPage(
            "Convert Files",
            "Convert PDF to Word, JPG, PNG & more",
            Icons.Default.SwapHoriz
        ),
        OnboardingPage(
            "Merge & Split",
            "Combine or split PDFs easily",
            Icons.Default.MergeType
        )
    )

    val pagerState = rememberPagerState(pageCount = { pages.size })

    Box(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .background(NavyDeep)
    ) {

        Column(
            modifier = Modifier.fillMaxSize()
        ) {

            // 🔹 Pager
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f)
            ) { page ->
                OnboardingPageUI(pages[page])
            }

            // 🔹 Dots Indicator
            DotsIndicator(
                totalDots = pages.size,
                selectedIndex = pagerState.currentPage
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 🔹 Button
            Button(
                onClick = {
                    if (pagerState.currentPage == pages.lastIndex) {
                        onFinish()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
                    .height(52.dp),
                shape = RoundedCornerShape(26.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BlueAccent)
            ) {
                Text(
                    if (pagerState.currentPage == pages.lastIndex)
                        "Get Started"
                    else
                        "Next",
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
@Composable
fun OnboardingPageUI(page: OnboardingPage) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Icon(
            imageVector = page.icon,
            contentDescription = null,
            tint = BlueAccent,
            modifier = Modifier.size(120.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = page.title,
            style = MaterialTheme.typography.headlineMedium,
            color = TextPrimary,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = page.description,
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary,
            textAlign = TextAlign.Center
        )
    }
}
@Composable
fun DotsIndicator(
    totalDots: Int,
    selectedIndex: Int
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        repeat(totalDots) { index ->
            Box(
                modifier = Modifier
                    .padding(4.dp)
                    .size(if (index == selectedIndex) 12.dp else 8.dp)
                    .clip(CircleShape)
                    .background(
                        if (index == selectedIndex) BlueAccent
                        else TextHint
                    )
            )
        }
    }
}