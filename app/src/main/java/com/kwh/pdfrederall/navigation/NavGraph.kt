package com.kwh.pdfrederall.navigation

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.kwh.pdfrederall.PdfViewModelFactory
import com.kwh.pdfrederall.data.model.PdfOperation
import com.kwh.pdfrederall.repositiory.LanguageRepository
import com.kwh.pdfrederall.repositiory.LanguageViewModelFactory
import com.kwh.pdfrederall.ui.screens.*
import com.kwh.pdfrederall.viewmodel.LanguageViewModel
import com.kwh.pdfrederall.viewmodel.PdfViewModel

@SuppressLint("UnrememberedGetBackStackEntry")
@Composable
fun AllPdfNavGraph(navController: NavHostController) {
    val context = LocalContext.current

    val viewModel: PdfViewModel = viewModel(

        factory = PdfViewModelFactory(context.applicationContext)
    )
    val startDestination = if (isFirstLaunch(context)) {
        Routes.INTRO
    } else {
        Routes.HOME
    }
    NavHost(
        navController = navController,
        startDestination = startDestination

    ) {

        composable(Routes.INTRO) {
            OnboardingScreen(
                onFinish = {
                    navController.navigate("home") {
                        popUpTo("onboarding") { inclusive = true }
                    }
                }
            )
        }
        composable(Routes.LANGUAGE) {

            val context = LocalContext.current
            val repo = remember { LanguageRepository(context) }

            val viewModel: LanguageViewModel = viewModel(
                factory = LanguageViewModelFactory(repo)
            )

            LanguageSelectionScreen(
                viewModel = viewModel,
                onContinue = {
                    setFirstLaunchDone(context)

                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.LANGUAGE) { inclusive = true }
                    }
                }
            )
        }
        composable(Routes.HOME) {
            HomeScreen(
                onNavigateToSelectFiles = { operation ->
                    viewModel.setOperation(operation)
                    viewModel.clearFiles()

                    navController.navigate(
                        Routes.selectFiles(operation.name)
                    )
                }
            )
        }
        composable(Routes.SETTINGS) {
            SettingsScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable(Routes.SELECT_FILES) { backStackEntry ->
            val operationStr = backStackEntry.arguments?.getString("operation") ?: "COMPRESS"
            val operation = PdfOperation.valueOf(operationStr)
            SelectFilesScreen(
                viewModel = viewModel,
                operation = operation,
                onBack = { navController.popBackStack() },
                onNext = {
                    when (operation) {
                        PdfOperation.COMPRESS -> navController.navigate(Routes.RECOMMENDED_ACTION)
                        PdfOperation.CONVERT -> navController.navigate(Routes.CONVERT_TO)
                        PdfOperation.MERGE -> {
                            viewModel.startMerge()
                            navController.navigate(Routes.PROCESSING)
                        }
                        PdfOperation.SPLIT -> navController.navigate(Routes.SPLIT_PDF)
                    }
                }
            )
        }

        composable(Routes.RECOMMENDED_ACTION) {
            RecommendedActionScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onCompress = { navController.navigate(Routes.COMPRESS_OPTIONS) },
                onConvert = { navController.navigate(Routes.CONVERT_TO) },
                onMerge = {
                    viewModel.startMerge()
                    navController.navigate(Routes.PROCESSING)
                }
            )
        }

        composable(Routes.COMPRESS_OPTIONS) {
            CompressOptionsScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onQualitySelected = { quality ->
                    viewModel.setCompressionQuality(quality)
                    navController.navigate(Routes.COMPRESS_PREVIEW)
                }
            )
        }

        composable(Routes.COMPRESS_PREVIEW) {
            CompressPreviewScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onCompressNow = {
                    viewModel.startCompression()
                    navController.navigate(Routes.PROCESSING)
                }
            )
        }

        composable(Routes.CONVERT_TO) {
            ConvertToScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onFormatSelected = { format ->
                    viewModel.setConvertFormat(format)
                    viewModel.startConversion()
                    navController.navigate(Routes.PROCESSING)
                }
            )
        }

        composable(Routes.SPLIT_PDF) {
            SplitPdfScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onSplitNow = {
                    viewModel.startSplit()
                    navController.navigate(Routes.PROCESSING)
                }
            )
        }

        composable(Routes.PROCESSING) {
            ProcessingScreen(
                viewModel = viewModel,
                onComplete = {
                    navController.navigate(Routes.SUCCESS) {
                        popUpTo(Routes.PROCESSING) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.SUCCESS) {
            SuccessScreen(
                viewModel = viewModel,
                onDownload = { /* handled inside screen */ },
                onShare = { /* handled inside screen */ },
                onGoHome = {
                    viewModel.resetProcessing()
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.HOME) { inclusive = true }
                    }
                }
            )
        }
    }
}
fun isFirstLaunch(context: Context): Boolean {
    val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    return prefs.getBoolean("first_launch", true)
}

fun setFirstLaunchDone(context: Context) {
    val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    prefs.edit().putBoolean("first_launch", false).apply()
}