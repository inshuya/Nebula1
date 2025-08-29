package com.example.onenoteclone.feature.cpnb

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.onenoteclone.ui.components.*
import com.example.onenoteclone.ui.theme.OneNoteCloneTheme
import com.example.onenoteclone.utils.TimeUtils
import com.example.onenoteclone.viewmodel.CopilotNotebooksViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

/**
 * Copilot Notebooks screen implementation
 */
@Composable
fun CopilotNotebooksScreen(
    onShowSnackbar: (String) -> Unit,
    viewModel: CopilotNotebooksViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val swipeRefreshState = rememberSwipeRefreshState(uiState.isRefreshing)
    
    // Handle error messages
    LaunchedEffect(uiState.error) {
        uiState.error?.let { error ->
            onShowSnackbar(error)
            viewModel.clearError()
        }
    }
    
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Top App Bar
        TopAppBarLarge(
            title = "Copilot Notebooks",
            navigationIcon = {
                IconButton(onClick = { onShowSnackbar("Menu clicked") }) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Menu",
                        tint = Color.White
                    )
                }
            }
        )
        
        SwipeRefresh(
            state = swipeRefreshState,
            onRefresh = viewModel::refresh,
            modifier = Modifier.fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 100.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // Suggested Notebooks Section
                item {
                    SuggestedNotebooksSection(
                        suggestions = uiState.suggestions,
                        onRefreshClick = { onShowSnackbar("Refresh suggestions") },
                        onViewAllClick = { onShowSnackbar("View all suggestions") },
                        onSuggestionClick = { suggestion ->
                            onShowSnackbar("Selected: ${suggestion.title}")
                        }
                    )
                }
                
                // Filters and View Toggle Section
                item {
                    FiltersSection(
                        selectedFilter = uiState.selectedFilter,
                        isListView = uiState.isListView,
                        onFilterChange = viewModel::setFilter,
                        onViewToggle = { viewModel.toggleViewMode() }
                    )
                }
                
                // Notebooks List Section
                items(uiState.filteredNotebooks) { notebook ->
                    NotebookListItem(
                        title = notebook.title,
                        isLocked = notebook.isLocked,
                        isShared = notebook.isShared,
                        groupCount = notebook.groupCount,
                        timeText = TimeUtils.formatRelativeTime(notebook.lastModified),
                        collaborators = notebook.collaborators,
                        thumbnailUrl = notebook.thumbnailUrl,
                        onClick = { onShowSnackbar("Notebook: ${notebook.title}") },
                        onMenuClick = { onShowSnackbar("Menu for: ${notebook.title}") }
                    )
                }
            }
        }
    }
}

@Composable
private fun SuggestedNotebooksSection(
    suggestions: List<com.example.onenoteclone.data.Suggestion>,
    onRefreshClick: () -> Unit,
    onViewAllClick: () -> Unit,
    onSuggestionClick: (com.example.onenoteclone.data.Suggestion) -> Unit
) {
    Column(
        modifier = Modifier.padding(top = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Section header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "✨",
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(
                    text = "Suggested Notebooks",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.semantics { heading() }
                )
                IconButton(
                    onClick = onRefreshClick,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Refresh suggestions",
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
            
            TextButton(onClick = onViewAllClick) {
                Text("View all")
            }
        }
        
        // Subtitle
        Text(
            text = "Get started on your new Notebook based on your recent focus areas and topics.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        
        // Suggestions carousel
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(suggestions) { suggestion ->
                SuggestionCard(
                    title = suggestion.title,
                    subtitle = suggestion.subtitle,
                    referencesCount = suggestion.referencesCount,
                    thumbnailUrl = suggestion.thumbnailUrl,
                    onClick = { onSuggestionClick(suggestion) }
                )
            }
        }
    }
}

@Composable
private fun FiltersSection(
    selectedFilter: CopilotNotebooksViewModel.FilterType,
    isListView: Boolean,
    onFilterChange: (CopilotNotebooksViewModel.FilterType) -> Unit,
    onViewToggle: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        PillTabs(
            items = listOf("All", "Favorites", "Shared with you"),
            selectedIndex = when (selectedFilter) {
                CopilotNotebooksViewModel.FilterType.ALL -> 0
                CopilotNotebooksViewModel.FilterType.FAVORITES -> 1
                CopilotNotebooksViewModel.FilterType.SHARED -> 2
            },
            onSelect = { index ->
                val filter = when (index) {
                    0 -> CopilotNotebooksViewModel.FilterType.ALL
                    1 -> CopilotNotebooksViewModel.FilterType.FAVORITES
                    2 -> CopilotNotebooksViewModel.FilterType.SHARED
                    else -> CopilotNotebooksViewModel.FilterType.ALL
                }
                onFilterChange(filter)
            }
        )
        
        ViewToggle(
            isListView = isListView,
            onToggle = { onViewToggle() }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CopilotNotebooksScreenPreview() {
    OneNoteCloneTheme {
        CopilotNotebooksScreen(
            onShowSnackbar = {}
        )
    }
}