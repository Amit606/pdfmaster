package com.kwh.pdfrederall

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.kwh.pdfrederall.navigation.AllPdfNavGraph
import com.kwh.pdfrederall.ui.theme.AllPdfReaderTheme
import com.kwh.pdfrederall.ui.theme.NavyDeep

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AllPdfReaderTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = NavyDeep
                ) {
                    val navController = rememberNavController()
                    AllPdfNavGraph(navController = navController)
                }
            }
        }
    }
}
