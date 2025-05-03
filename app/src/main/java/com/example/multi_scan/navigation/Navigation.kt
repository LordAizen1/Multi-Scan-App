package com.example.multi_scan.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.multi_scan.ui.screens.home.DocScannerScreen
import com.example.multi_scan.ui.screens.home.HomeScreen
import com.example.multi_scan.ui.screens.landmark.LandmarkScreen
import com.example.multi_scan.ui.viewmodels.PdfViewModel

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object DocScanner : Screen("doc_scanner")
    object LandmarkRecognition : Screen("landmark_recognition")
}

@Composable
fun AppNavHost(
    navController: NavHostController,
    pdfViewModel: PdfViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                pdfViewModel = pdfViewModel,
                onNavigateToDocScanner = { navController.navigate(Screen.DocScanner.route) },
                onNavigateToLandmarkRecognition = { navController.navigate(Screen.LandmarkRecognition.route) }
            )
        }
        
        composable(Screen.DocScanner.route) {
            DocScannerScreen(pdfViewModel = pdfViewModel)
        }
        
        composable(Screen.LandmarkRecognition.route) {
            LandmarkScreen()
        }
    }
} 