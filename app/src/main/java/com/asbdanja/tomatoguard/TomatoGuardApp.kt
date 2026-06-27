package com.asbdanja.tomatoguard

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.asbdanja.tomatoguard.navigation.BottomNavBar
import com.asbdanja.tomatoguard.navigation.Screen
import com.asbdanja.tomatoguard.navigation.TomatoGuardNavGraph
import com.asbdanja.tomatoguard.ui.HomeScreen
import com.asbdanja.tomatoguard.viewmodel.ScanViewModel

@Composable
fun TomatoGuardApp(viewModel: ScanViewModel) {
    val navController = rememberNavController()
    // Track current route to hide elements
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            // Don't show navigation on Splash Screen
            if (currentRoute != Screen.Splash.route) {
                BottomNavBar(navController)
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(if (currentRoute == Screen.Splash.route) PaddingValues(0.dp) else padding)) {
            TomatoGuardNavGraph(
                navController = navController,
                viewModel     = viewModel
            )
        }
    }
}



@Preview(
    showBackground = true,
    showSystemUi = true,
    name = "Full App - Start (Splash)",
)
@Composable
fun TomatoGuardAppSplashPreview() {
    MaterialTheme {
        TomatoGuardApp(
            viewModel = viewModel()
        )
    }
}


@Preview(
    showBackground = true,
    showSystemUi = true,
    name = "App Shell - Home State",
)
@Composable
fun TomatoGuardAppHomePreview() {
    val navController = rememberNavController()
    MaterialTheme {
        Scaffold(
            bottomBar = {
                // We call the actual BottomNavBar from your navigation package
                BottomNavBar(navController)
            }
        ) { padding ->
            // Simulating the content area
            Box(modifier = Modifier.padding(padding)) {
                // You can put a placeholder or the actual HomeScreen here
                 HomeScreen(viewModel = viewModel())
                // Note: Real ViewModels with ONNX might crash previews
            }
        }
    }
}