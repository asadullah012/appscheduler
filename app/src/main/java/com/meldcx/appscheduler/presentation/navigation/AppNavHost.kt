package com.meldcx.appscheduler.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.meldcx.appscheduler.presentation.screen.AppListScreen
import com.meldcx.appscheduler.presentation.screen.AppScheduleScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = BottomNavItem.AppScreen.route,
        modifier = modifier
    ) {
        composable("all_app") {
            AppListScreen()
        }
        composable(
            route = BottomNavItem.ScheduleScreen.route
        ) {
            AppScheduleScreen()
        }
    }
}
