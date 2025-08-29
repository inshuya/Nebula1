package com.example.onenoteclone.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.onenoteclone.ui.theme.OneNoteCloneTheme

/**
 * Toggle between list and grid view
 */
@Composable
fun ViewToggle(
    isListView: Boolean,
    onToggle: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(20.dp)
            )
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        // List view button
        IconButton(
            onClick = { onToggle(true) },
            modifier = Modifier
                .size(32.dp)
                .background(
                    color = if (isListView) {
                        MaterialTheme.colorScheme.surface
                    } else {
                        Color.Transparent
                    },
                    shape = CircleShape
                )
        ) {
            Icon(
                imageVector = Icons.Default.List,
                contentDescription = "List view",
                modifier = Modifier.size(16.dp),
                tint = if (isListView) {
                    MaterialTheme.colorScheme.onSurface
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                }
            )
        }
        
        // Grid view button
        IconButton(
            onClick = { onToggle(false) },
            modifier = Modifier
                .size(32.dp)
                .background(
                    color = if (!isListView) {
                        MaterialTheme.colorScheme.surface
                    } else {
                        Color.Transparent
                    },
                    shape = CircleShape
                )
        ) {
            Icon(
                imageVector = Icons.Default.GridView,
                contentDescription = "Grid view",
                modifier = Modifier.size(16.dp),
                tint = if (!isListView) {
                    MaterialTheme.colorScheme.onSurface
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ViewTogglePreview() {
    OneNoteCloneTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ViewToggle(
                isListView = true,
                onToggle = {}
            )
            
            ViewToggle(
                isListView = false,
                onToggle = {}
            )
        }
    }
}