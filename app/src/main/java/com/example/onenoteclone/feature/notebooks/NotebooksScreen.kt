package com.example.onenoteclone.feature.notebooks

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.onenoteclone.ui.components.*
import com.example.onenoteclone.ui.theme.OneNoteCloneTheme
import com.example.onenoteclone.utils.TimeUtils
import com.example.onenoteclone.viewmodel.NotebooksViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

/**
 * Notebooks screen implementation
 */
@Composable
fun NotebooksScreen(
    onShowSnackbar: (String) -> Unit,
    viewModel: NotebooksViewModel = viewModel()
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
            title = "Notebooks"
        )
        
        // Tab row
        PillTabs(
            items = listOf("Recent pages", "Notebook list"),
            selectedIndex = when (uiState.selectedTab) {
                NotebooksViewModel.TabType.RECENT_PAGES -> 0
                NotebooksViewModel.TabType.NOTEBOOK_LIST -> 1
            },
            onSelect = { index ->
                val tab = when (index) {
                    0 -> NotebooksViewModel.TabType.RECENT_PAGES
                    1 -> NotebooksViewModel.TabType.NOTEBOOK_LIST
                    else -> NotebooksViewModel.TabType.RECENT_PAGES
                }
                viewModel.setSelectedTab(tab)
            },
            modifier = Modifier.padding(16.dp)
        )
        
        SwipeRefresh(
            state = swipeRefreshState,
            onRefresh = viewModel::refresh,
            modifier = Modifier.fillMaxSize()
        ) {
            when (uiState.selectedTab) {
                NotebooksViewModel.TabType.RECENT_PAGES -> {
                    RecentPagesGrid(
                        pages = uiState.recentPages,
                        onPageClick = { page ->
                            onShowSnackbar("Page: ${page.title}")
                        }
                    )
                }
                NotebooksViewModel.TabType.NOTEBOOK_LIST -> {
                    NotebooksList(
                        notebooks = uiState.notebooks,
                        onNotebookClick = { notebook ->
                            onShowSnackbar("Notebook: ${notebook.title}")
                        },
                        onCreateNotebook = {
                            onShowSnackbar("Create new notebook")
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun RecentPagesGrid(
    pages: List<com.example.onenoteclone.data.NotebookPage>,
    onPageClick: (com.example.onenoteclone.data.NotebookPage) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(pages) { page ->
            MiniNotebookCard(
                title = page.title,
                breadcrumb = page.breadcrumb,
                dateText = TimeUtils.formatDate(page.lastModified),
                thumbnailUrl = page.thumbnailUrl,
                onClick = { onPageClick(page) }
            )
        }
    }
}

@Composable
private fun NotebooksList(
    notebooks: List<com.example.onenoteclone.data.Notebook>,
    onNotebookClick: (com.example.onenoteclone.data.Notebook) -> Unit,
    onCreateNotebook: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 16.dp, horizontal = 0.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Create new notebook button
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    onClick = onCreateNotebook,
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier.size(40.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add notebook",
                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
                
                Text(
                    text = "New notebook",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Medium
                    ),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
        
        // Notebooks list
        items(notebooks) { notebook ->
            NotebookListItem(
                title = notebook.title,
                isLocked = notebook.isLocked,
                isShared = notebook.isShared,
                groupCount = notebook.groupCount,
                timeText = TimeUtils.formatRelativeTime(notebook.lastModified),
                collaborators = notebook.collaborators,
                thumbnailUrl = notebook.thumbnailUrl,
                onClick = { onNotebookClick(notebook) },
                onMenuClick = { /* Handle menu */ }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NotebooksScreenPreview() {
    OneNoteCloneTheme {
        NotebooksScreen(
            onShowSnackbar = {}
        )
    }
}