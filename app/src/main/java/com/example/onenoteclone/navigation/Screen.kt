package com.example.onenoteclone.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Note
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.StickyNote2
import androidx.compose.material.icons.outlined.SmartToy
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Navigation destinations for the app
 */
sealed class Screen(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object CopilotNotebooks : Screen(
        route = "cpnb",
        title = "Copilot Notebooks",
        icon = Icons.Outlined.SmartToy
    )
    
    object Notebooks : Screen(
        route = "notebooks", 
        title = "Notebooks",
        icon = Icons.Default.Note
    )
    
    object StickyNotes : Screen(
        route = "sticky",
        title = "Sticky Notes", 
        icon = Icons.Default.StickyNote2
    )
    
    object Search : Screen(
        route = "search",
        title = "Search",
        icon = Icons.Default.Search
    )
}

/**
 * List of bottom navigation items
 */
val bottomNavItems = listOf(
    Screen.CopilotNotebooks,
    Screen.Notebooks,
    Screen.StickyNotes,
    Screen.Search
)