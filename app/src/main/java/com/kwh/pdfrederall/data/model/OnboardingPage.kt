package com.kwh.pdfrederall.data.model

import com.kwh.pdfrederall.R


data class OnboardingPage(
    val title: String,
    val description: String,
    val image: Int // drawable resource
)
val pages = listOf(
    OnboardingPage(
        "Seamless Merging",
        "Your Ultimate Toolkit for Digital Excellence: Merge files with confidence using our streamlined platform",
        R.drawable.ic_launcher
    ),
    OnboardingPage(
        "Image to PDF",
        "Convert Images to PDF with confidence using our streamlined platform",
        R.drawable.ic_launcher
    ),
    OnboardingPage(
        "Compression Magic",
        "Compress documents with confidence using our streamlined platform",
        R.drawable.ic_launcher
    )
)