package com.kwh.pdfrederall.navigation

object Routes {
    const val INTRO = "intro"

    const val HOME = "home"
    const val SELECT_FILES = "select_files/{operation}"
    const val RECOMMENDED_ACTION = "recommended_action"
    const val COMPRESS_OPTIONS = "compress_options"
    const val COMPRESS_PREVIEW = "compress_preview"
    const val CONVERT_TO = "convert_to"
    const val SPLIT_PDF = "split_pdf"
    const val PROCESSING = "processing"
    const val SUCCESS = "success"
    const val LANGUAGE = "language"
    const val SETTINGS = "settings"

    fun selectFiles(operation: String) = "select_files/$operation"
}
