package com.example.onenoteclone.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.onenoteclone.data.Notebook
import com.example.onenoteclone.data.Suggestion
import com.example.onenoteclone.repository.FakeRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * ViewModel for the Copilot Notebooks screen
 */
class CopilotNotebooksViewModel(
    private val repository: FakeRepository = FakeRepository.getInstance()
) : ViewModel() {
    
    data class UiState(
        val suggestions: List<Suggestion> = emptyList(),
        val notebooks: List<Notebook> = emptyList(),
        val filteredNotebooks: List<Notebook> = emptyList(),
        val isRefreshing: Boolean = false,
        val selectedFilter: FilterType = FilterType.ALL,
        val isListView: Boolean = true,
        val error: String? = null
    )
    
    enum class FilterType {
        ALL, FAVORITES, SHARED
    }
    
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()
    
    init {
        loadData()
    }
    
    private fun loadData() {
        viewModelScope.launch {
            combine(
                repository.suggestions,
                repository.notebooks
            ) { suggestions, notebooks ->
                _uiState.value = _uiState.value.copy(
                    suggestions = suggestions,
                    notebooks = notebooks,
                    filteredNotebooks = filterNotebooks(notebooks, _uiState.value.selectedFilter)
                )
            }.collect()
        }
    }
    
    fun refresh() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isRefreshing = true)
            try {
                repository.refreshSuggestions()
                repository.refreshNotebooks()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            } finally {
                _uiState.value = _uiState.value.copy(isRefreshing = false)
            }
        }
    }
    
    fun setFilter(filter: FilterType) {
        _uiState.value = _uiState.value.copy(
            selectedFilter = filter,
            filteredNotebooks = filterNotebooks(_uiState.value.notebooks, filter)
        )
    }
    
    fun toggleViewMode() {
        _uiState.value = _uiState.value.copy(
            isListView = !_uiState.value.isListView
        )
    }
    
    private fun filterNotebooks(notebooks: List<Notebook>, filter: FilterType): List<Notebook> {
        return when (filter) {
            FilterType.ALL -> notebooks
            FilterType.FAVORITES -> notebooks.filter { it.isFavorite }
            FilterType.SHARED -> notebooks.filter { it.isShared }
        }
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}