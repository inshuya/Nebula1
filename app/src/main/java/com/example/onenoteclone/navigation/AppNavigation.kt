package com.example.onenoteclone.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.onenoteclone.feature.cpnb.CopilotNotebooksScreen
import com.example.onenoteclone.feature.notebooks.NotebooksScreen
import com.example.onenoteclone.feature.search.SearchScreen
import com.example.onenoteclone.feature.sticky.StickyNotesScreen

/**
 * Navigation graph for the app
 */
@Composable
fun AppNavigation(
    navController: NavHostController,
    paddingValues: PaddingValues,
    onShowSnackbar: (String) -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = Screen.CopilotNotebooks.route
    ) {
        composable(Screen.CopilotNotebooks.route) {
            CopilotNotebooksScreen(
                onShowSnackbar = onShowSnackbar
            )
        }
        
        composable(Screen.Notebooks.route) {
            NotebooksScreen(
                onShowSnackbar = onShowSnackbar
            )
        }
        
        composable(Screen.StickyNotes.route) {
            StickyNotesScreen(
                onShowSnackbar = onShowSnackbar
            )
        }
        
        composable(Screen.Search.route) {
            SearchScreen(
                onShowSnackbar = onShowSnackbar
            )
        }
    }
}