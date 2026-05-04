package com.kwh.pdfrederall.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kwh.pdfrederall.repositiory.LanguageRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LanguageViewModel(
    private val repo : LanguageRepository
): ViewModel() {
    private val _language = MutableStateFlow("en")
    val language: StateFlow<String> = _language
    init {
        viewModelScope.launch {
            repo.languageFlow.collect {
                _language.value = it
            }
        }
    }
        fun changeLanguage(lang: String) {
            viewModelScope.launch {
                repo.saveLanguage(lang)
            }
        }

}