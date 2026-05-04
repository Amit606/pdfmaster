package com.kwh.pdfrederall.repositiory

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore("settings")

object PrefKeys {
    val LANGUAGE = stringPreferencesKey("language")
}
class LanguageRepository(private  val context: Context) {
    val languageFlow: Flow<String> = context.dataStore.data
        .map { it[PrefKeys.LANGUAGE] ?: "en" }

    suspend fun saveLanguage(lang: String) {
        context.dataStore.edit {
            it[PrefKeys.LANGUAGE] = lang
        }
    }
}