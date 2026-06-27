package com.asbdanja.tomatoguard.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.asbdanja.tomatoguard.theme.ForestGreen
import com.asbdanja.tomatoguard.ui.AboutScreen
import com.asbdanja.tomatoguard.ui.HistoryScreen
import com.asbdanja.tomatoguard.ui.HomeScreen
import com.asbdanja.tomatoguard.viewmodel.ScanViewModel
import com.asbdanja.tomatoguard.ui.SplashScreen


sealed class Screen(val route: String, val label: String = "", val icon: ImageVector = Icons.Outlined.Home) {
    object Splash : Screen("splash") // Initial entry
    object Home : Screen("home", "Home", Icons.Outlined.Home)
    object History : Screen("history", "History", Icons.Outlined.History)
    object About : Screen("about", "About", Icons.Outlined.Info)
}

@Composable
fun TomatoGuardNavGraph(
    navController: NavHostController,
    viewModel: ScanViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None }
    ) {
        composable(Screen.Splash.route) {
           SplashScreen(onNavigateToHome = {
                navController.navigate(Screen.Home.route) {
                    popUpTo(Screen.Splash.route) { inclusive = true } // Clear splash from stack
                }
            })
        }
        composable(Screen.Home.route) {
            HomeScreen(viewModel = viewModel)
        }

        composable(Screen.History.route) {
            HistoryScreen(viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.About.route) {
            AboutScreen(
                onBack = { navController.popBackStack()}
            )
        }

    }
}

@Composable
fun BottomNavBar(navController: NavController) {
    val screens = listOf(
        Screen.Home,
        Screen.History,
        Screen.About
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 0.dp
    ) {
        screens.forEach { screen ->
            NavigationBarItem(
                icon = {
                    Icon(
                        screen.icon,
                        contentDescription = screen.label,
                        modifier = if (currentRoute == screen.route) {
                            Modifier
                        } else {
                            Modifier
                        }
                    )
                },
                label = { Text(screen.label) },
                selected = currentRoute == screen.route,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = ForestGreen,
                    selectedTextColor = ForestGreen,
                    unselectedIconColor = Color.Gray,
                    unselectedTextColor = Color.Gray
                ),
                onClick = {
                    if (currentRoute != screen.route) {
                        navController.navigate(screen.route) {
                            // 2. IMPORTANT: Always pop up to Home, not Splash
                            popUpTo(Screen.Home.route) {
                                saveState = true
                            }
                            // 3. Avoid multiple copies of the same screen
                            launchSingleTop = true
                            // 4. Restore the scroll position/state
                            restoreState = true
                        }
                    }
                }
            )
        }
    }
}