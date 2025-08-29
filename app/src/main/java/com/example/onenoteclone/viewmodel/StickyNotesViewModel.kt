package com.example.onenoteclone.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.onenoteclone.data.StickyNote
import com.example.onenoteclone.repository.FakeRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * ViewModel for the Sticky Notes screen
 */
class StickyNotesViewModel(
    private val repository: FakeRepository = FakeRepository.getInstance()
) : ViewModel() {
    
    data class UiState(
        val stickyNotes: List<StickyNote> = emptyList(),
        val isRefreshing: Boolean = false,
        val error: String? = null
    )
    
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()
    
    init {
        loadData()
    }
    
    private fun loadData() {
        viewModelScope.launch {
            repository.stickyNotes.collect { stickyNotes ->
                _uiState.value = _uiState.value.copy(stickyNotes = stickyNotes)
            }
        }
    }
    
    fun refresh() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isRefreshing = true)
            try {
                repository.refreshStickyNotes()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            } finally {
                _uiState.value = _uiState.value.copy(isRefreshing = false)
            }
        }
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}