package com.example.onenoteclone.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.onenoteclone.data.Notebook
import com.example.onenoteclone.data.NotebookPage
import com.example.onenoteclone.repository.FakeRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * ViewModel for the Notebooks screen
 */
class NotebooksViewModel(
    private val repository: FakeRepository = FakeRepository.getInstance()
) : ViewModel() {
    
    data class UiState(
        val recentPages: List<NotebookPage> = emptyList(),
        val notebooks: List<Notebook> = emptyList(),
        val isRefreshing: Boolean = false,
        val selectedTab: TabType = TabType.RECENT_PAGES,
        val error: String? = null
    )
    
    enum class TabType {
        RECENT_PAGES, NOTEBOOK_LIST
    }
    
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()
    
    init {
        loadData()
    }
    
    private fun loadData() {
        viewModelScope.launch {
            combine(
                repository.recentPages,
                repository.notebooks
            ) { recentPages, notebooks ->
                _uiState.value = _uiState.value.copy(
                    recentPages = recentPages,
                    notebooks = notebooks
                )
            }.collect()
        }
    }
    
    fun refresh() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isRefreshing = true)
            try {
                repository.refreshRecentPages()
                repository.refreshNotebooks()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            } finally {
                _uiState.value = _uiState.value.copy(isRefreshing = false)
            }
        }
    }
    
    fun setSelectedTab(tab: TabType) {
        _uiState.value = _uiState.value.copy(selectedTab = tab)
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}