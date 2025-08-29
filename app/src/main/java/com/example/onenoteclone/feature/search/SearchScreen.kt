package com.example.onenoteclone.feature.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.onenoteclone.data.SearchResult
import com.example.onenoteclone.data.SearchResultType
import com.example.onenoteclone.ui.components.*
import com.example.onenoteclone.ui.theme.OneNoteCloneTheme
import com.example.onenoteclone.utils.TimeUtils
import com.example.onenoteclone.viewmodel.SearchViewModel

/**
 * Search screen implementation
 */
@Composable
fun SearchScreen(
    onShowSnackbar: (String) -> Unit,
    viewModel: SearchViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
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
            title = "Search"
        )
        
        // Search field
        SearchField(
            query = uiState.query,
            onQueryChange = viewModel::updateQuery,
            placeholder = "Search notebooks, pages, and notes",
            onSearch = { viewModel.searchContent(it) },
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
        
        // Content based on search state
        if (uiState.query.isBlank()) {
            EmptySearchState(
                recentSearches = uiState.recentSearches,
                onRecentSearchClick = viewModel::onRecentSearchClick
            )
        } else if (uiState.isSearching) {
            SearchingState()
        } else if (uiState.searchResults.isEmpty()) {
            NoResultsState(query = uiState.query)
        } else {
            SearchResults(
                results = uiState.searchResults,
                query = uiState.query,
                onResultClick = { result ->
                    onShowSnackbar("Selected: ${result.title}")
                }
            )
        }
    }
}

@Composable
private fun EmptySearchState(
    recentSearches: List<String>,
    onRecentSearchClick: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Recent searches
        if (recentSearches.isNotEmpty()) {
            item {
                Text(
                    text = "Recent searches",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    modifier = Modifier.semantics { heading() }
                )
            }
            
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(recentSearches) { search ->
                        TagChip(
                            text = search,
                            modifier = Modifier.clickable { onRecentSearchClick(search) },
                            backgroundColor = MaterialTheme.colorScheme.surfaceVariant,
                            textColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
        
        // Empty state illustration
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                )
                
                Text(
                    text = "Search across all your notebooks",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                Text(
                    text = "Find pages, notes, and content quickly",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                )
            }
        }
    }
}

@Composable
private fun SearchingState() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        repeat(6) {
            ShimmerPlaceholder(height = 60)
        }
    }
}

@Composable
private fun NoResultsState(query: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "No results found",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.SemiBold
            )
        )
        
        Text(
            text = "Try searching for different keywords or check your spelling",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun SearchResults(
    results: List<SearchResult>,
    query: String,
    onResultClick: (SearchResult) -> Unit
) {
    val groupedResults = results.groupBy { it.type }
    
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        groupedResults.forEach { (type, typeResults) ->
            item {
                Text(
                    text = when (type) {
                        SearchResultType.COPILOT_NOTEBOOK -> "Copilot Notebooks"
                        SearchResultType.NOTEBOOK_PAGE -> "Notebook Pages"
                        SearchResultType.STICKY_NOTE -> "Sticky Notes"
                    },
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .semantics { heading() }
                )
            }
            
            items(typeResults) { result ->
                SearchResultItem(
                    result = result,
                    query = query,
                    onClick = { onResultClick(result) }
                )
            }
        }
    }
}

@Composable
private fun SearchResultItem(
    result: SearchResult,
    query: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = highlightQuery(result.title, query),
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            
            if (result.content.isNotEmpty()) {
                Text(
                    text = highlightQuery(result.content, query),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = result.source,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
                
                Text(
                    text = TimeUtils.formatRelativeTime(result.lastModified),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun highlightQuery(text: String, query: String) = buildAnnotatedString {
    val startIndex = text.indexOf(query, ignoreCase = true)
    if (startIndex != -1) {
        append(text.substring(0, startIndex))
        withStyle(
            style = SpanStyle(
                background = MaterialTheme.colorScheme.primaryContainer,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                fontWeight = FontWeight.Medium
            )
        ) {
            append(text.substring(startIndex, startIndex + query.length))
        }
        append(text.substring(startIndex + query.length))
    } else {
        append(text)
    }
}

@Preview(showBackground = true)
@Composable
fun SearchScreenPreview() {
    OneNoteCloneTheme {
        SearchScreen(
            onShowSnackbar = {}
        )
    }
}