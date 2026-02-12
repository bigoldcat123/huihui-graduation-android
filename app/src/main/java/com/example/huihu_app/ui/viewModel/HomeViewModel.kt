package com.example.huihu_app.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.huihu_app.data.repository.LocalStoreRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class HomeUiState(
    val selectedTab: Int = 0
)

class HomeViewModel(
    private val localStoreRepository: LocalStoreRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            if (localStoreRepository.consumeOpenFoodTabOnce()) {
                _uiState.update { it.copy(selectedTab = 1) }
            }
        }
    }

    fun selectTab(tab: Int) {
        _uiState.update {
            it.copy(selectedTab = tab)
        }
    }

    fun logout() {
        viewModelScope.launch {
            localStoreRepository.logout()
        }
    }
}
