package com.example.onenoteclone.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.onenoteclone.data.SearchResult
import com.example.onenoteclone.repository.FakeRepository
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * ViewModel for the Search screen
 */
@OptIn(FlowPreview::class)
class SearchViewModel(
    private val repository: FakeRepository = FakeRepository.getInstance()
) : ViewModel() {
    
    data class UiState(
        val query: String = "",
        val searchResults: List<SearchResult> = emptyList(),
        val recentSearches: List<String> = listOf(
            "mobile app",
            "marketing",
            "team meeting",
            "product launch"
        ),
        val isSearching: Boolean = false,
        val error: String? = null
    )
    
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()
    
    init {
        // Set up automatic search with debouncing
        _uiState
            .map { it.query }
            .distinctUntilChanged()
            .debounce(300) // Wait 300ms after user stops typing
            .filter { it.isNotBlank() && it.length >= 2 }
            .onEach { query ->
                searchContent(query)
            }
            .launchIn(viewModelScope)
    }
    
    fun updateQuery(query: String) {
        _uiState.value = _uiState.value.copy(
            query = query,
            searchResults = if (query.isBlank()) emptyList() else _uiState.value.searchResults
        )
    }
    
    fun searchContent(query: String) {
        if (query.isBlank()) return
        
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSearching = true)
            try {
                repository.searchContent(query).collect { results ->
                    _uiState.value = _uiState.value.copy(
                        searchResults = results,
                        isSearching = false
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message,
                    isSearching = false
                )
            }
        }
    }
    
    fun onRecentSearchClick(query: String) {
        updateQuery(query)
        searchContent(query)
    }
    
    fun clearSearch() {
        _uiState.value = _uiState.value.copy(
            query = "",
            searchResults = emptyList()
        )
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}