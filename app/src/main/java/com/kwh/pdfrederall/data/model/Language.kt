package com.kwh.pdfrederall.data.model

data class Language(val code : String, val name: String,
                         val flag: String // emoji flag (simple & lightweight)
)

val languages = listOf(

    // 🌍 Global
    Language("en", "English", "🇬🇧"),
    Language("es", "Español", "🇪🇸"),
    Language("fr", "Français", "🇫🇷"),
    Language("de", "Deutsch", "🇩🇪"),
    Language("it", "Italiano", "🇮🇹"),
    Language("pt", "Português", "🇵🇹"),

    // 🇮🇳 India (Top languages)
    Language("hi", "हिन्दी", "🇮🇳"),       // Hindi
    Language("bn", "বাংলা", "🇮🇳"),       // Bengali
    Language("ta", "தமிழ்", "🇮🇳"),       // Tamil
    Language("te", "తెలుగు", "🇮🇳"),      // Telugu
    Language("mr", "मराठी", "🇮🇳"),       // Marathi
    Language("gu", "ગુજરાતી", "🇮🇳"),     // Gujarati
    Language("kn", "ಕನ್ನಡ", "🇮🇳"),       // Kannada
    Language("ml", "മലയാളം", "🇮🇳"),     // Malayalam
    Language("pa", "ਪੰਜਾਬੀ", "🇮🇳"),      // Punjabi
    Language("or", "ଓଡ଼ିଆ", "🇮🇳")        // Odia
)

