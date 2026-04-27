package com.kwh.pdfrederall

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kwh.pdfrederall.viewmodel.PdfViewModel

class PdfViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PdfViewModel::class.java)) {
            return PdfViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}