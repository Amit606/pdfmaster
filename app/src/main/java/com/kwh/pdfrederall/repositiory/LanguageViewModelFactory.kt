package com.kwh.pdfrederall.repositiory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kwh.pdfrederall.viewmodel.LanguageViewModel

class LanguageViewModelFactory(
    private val repo: LanguageRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LanguageViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LanguageViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}