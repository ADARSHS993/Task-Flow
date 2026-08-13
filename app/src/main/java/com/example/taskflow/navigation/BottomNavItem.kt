package com.example.taskflow.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.TabletMac
import androidx.compose.material.icons.filled.Task
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val route : String,
    val title : String,
    val icon : ImageVector
){
    data object Home : BottomNavItem(route = AppDestination.Home.route,
         title = "Home",
         icon = Icons.Default.Home
    )

    data object Tasks : BottomNavItem(route = AppDestination.Task.route,
        title = "Task",
        icon = Icons.Default.Task
    )

    data object Calendar : BottomNavItem(route = AppDestination.Calendar.route,
        title = "Calendar",
        icon = Icons.Default.DateRange)

    data object Stats : BottomNavItem(route = AppDestination.Stats.route,
        title = "Stats",
        icon = Icons.Default.Analytics)

    data object Profile : BottomNavItem(route = AppDestination.Profile.route,
        title = "Profile",
        icon = Icons.Default.Person)

}