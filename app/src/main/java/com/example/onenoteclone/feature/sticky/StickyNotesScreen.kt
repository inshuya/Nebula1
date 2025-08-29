package com.example.onenoteclone.feature.sticky

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.onenoteclone.ui.components.*
import com.example.onenoteclone.ui.theme.OneNoteCloneTheme
import com.example.onenoteclone.utils.TimeUtils
import com.example.onenoteclone.viewmodel.StickyNotesViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

/**
 * Sticky Notes screen implementation with masonry grid
 */
@Composable
fun StickyNotesScreen(
    onShowSnackbar: (String) -> Unit,
    viewModel: StickyNotesViewModel = viewModel()
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
    
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Top App Bar
            TopAppBarLarge(
                title = "Sticky Notes"
            )
            
            SwipeRefresh(
                state = swipeRefreshState,
                onRefresh = viewModel::refresh,
                modifier = Modifier.fillMaxSize()
            ) {
                LazyVerticalStaggeredGrid(
                    columns = StaggeredGridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalItemSpacing = 12.dp
                ) {
                    items(uiState.stickyNotes) { note ->
                        StickyNoteCard(
                            title = note.title,
                            content = note.content,
                            color = note.color,
                            dateText = TimeUtils.formatDate(note.createdAt),
                            photoUrl = note.photoUrl,
                            hasReminder = note.hasReminder,
                            onClick = { onShowSnackbar("Note: ${note.title}") }
                        )
                    }
                }
            }
        }
        
        // Floating action pods
        FloatingActionPods(
            onCameraClick = { onShowSnackbar("Camera clicked") },
            onEditClick = { onShowSnackbar("Edit clicked") },
            onCheckboxClick = { onShowSnackbar("Checkbox clicked") },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .padding(bottom = 80.dp) // Account for bottom navigation
        )
    }
}

@Composable
private fun FloatingActionPods(
    onCameraClick: () -> Unit,
    onEditClick: () -> Unit,
    onCheckboxClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.End
    ) {
        // Mini action buttons
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            MiniFloatingActionButton(
                icon = Icons.Default.CameraAlt,
                contentDescription = "Camera",
                onClick = onCameraClick
            )
            
            MiniFloatingActionButton(
                icon = Icons.Default.Edit,
                contentDescription = "Edit",
                onClick = onEditClick
            )
            
            MiniFloatingActionButton(
                icon = Icons.Default.CheckBox,
                contentDescription = "Checkbox",
                onClick = onCheckboxClick
            )
        }
    }
}

@Composable
private fun MiniFloatingActionButton(
    icon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    FloatingActionButton(
        onClick = onClick,
        modifier = modifier.size(40.dp),
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
        contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
        elevation = FloatingActionButtonDefaults.elevation(
            defaultElevation = 6.dp
        )
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun StickyNotesScreenPreview() {
    OneNoteCloneTheme {
        StickyNotesScreen(
            onShowSnackbar = {}
        )
    }
}