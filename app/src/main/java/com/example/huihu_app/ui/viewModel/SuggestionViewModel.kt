package com.example.huihu_app.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.huihu_app.data.model.Suggestion
import com.example.huihu_app.data.repository.SuggestionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SuggestionUiState(
    val isLoading: Boolean = false,
    val suggestions: List<Suggestion> = emptyList(),
    val error: String? = null
)

class SuggestionViewModel(
    private val suggestionRepository: SuggestionRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(SuggestionUiState())
    val uiState = _uiState.asStateFlow()

    fun loadMySuggestions(token: String) {
        if (_uiState.value.isLoading) return
        _uiState.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            val response = suggestionRepository.mySuggestions(token)
            if (!response.isSuccess()) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = response.message
                    )
                }
                return@launch
            }
            _uiState.update {
                it.copy(
                    isLoading = false,
                    suggestions = response.data.orEmpty(),
                    error = null
                )
            }
        }
    }
}
