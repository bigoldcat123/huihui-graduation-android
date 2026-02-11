package com.example.huihu_app.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.huihu_app.data.model.Food
import com.example.huihu_app.data.repository.FoodRepository
import com.example.huihu_app.data.repository.LocalStoreRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class NewPersonUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val currentRound: Int = 0,
    val foods: List<Food> = emptyList(),
    val selectedCurrentRound: Set<Int> = emptySet(),
    val allReceivedFoodIds: Set<Int> = emptySet(),
    val allSelectedFoodIds: Set<Int> = emptySet(),
    val isCompleted: Boolean = false
)

class NewPersonViewModel(
    private val foodRepository: FoodRepository,
    private val localStoreRepository: LocalStoreRepository
) : ViewModel() {

    companion object {
        private const val MAX_ROUNDS = 2
    }

    private val _uiState = MutableStateFlow(NewPersonUiState())
    val uiState = _uiState.asStateFlow()

    private var authToken: String? = null
    private var lastFoodIds: List<Int> = emptyList()
    private var lastSelectedIds: List<Int> = emptyList()

    fun start(token: String) {
        if (authToken != null) return
        authToken = token
        fetchConsecutiveSuggest(foodIds = emptyList(), selectedFoodIds = emptyList())
    }

    fun toggleFoodSelection(foodId: Int) {
        _uiState.update { state ->
            val newSelected = if (state.selectedCurrentRound.contains(foodId)) {
                state.selectedCurrentRound - foodId
            } else {
                state.selectedCurrentRound + foodId
            }
            state.copy(selectedCurrentRound = newSelected)
        }
    }

    fun submitCurrentRound() {
        val state = _uiState.value
        if (state.isLoading || state.isCompleted || state.selectedCurrentRound.isEmpty()) return

        val mergedSelected = state.allSelectedFoodIds + state.selectedCurrentRound

        if (state.currentRound >= MAX_ROUNDS) {
            _uiState.update {
                it.copy(
                    allSelectedFoodIds = mergedSelected,
                    isCompleted = true
                )
            }
            viewModelScope.launch {
                localStoreRepository.markOnboardingCompleted()
            }
            return
        }

        _uiState.update {
            it.copy(allSelectedFoodIds = mergedSelected)
        }

        fetchConsecutiveSuggest(
            foodIds = state.allReceivedFoodIds.toList(),
            selectedFoodIds = mergedSelected.toList()
        )
    }

    fun retry() {
        if (_uiState.value.isLoading || authToken == null) return
        fetchConsecutiveSuggest(lastFoodIds, lastSelectedIds)
    }

    private fun fetchConsecutiveSuggest(foodIds: List<Int>, selectedFoodIds: List<Int>) {
        val token = authToken ?: return
        lastFoodIds = foodIds
        lastSelectedIds = selectedFoodIds

        _uiState.update {
            it.copy(isLoading = true, error = null)
        }

        viewModelScope.launch {
            val res = foodRepository.consecutiveSuggest(token, foodIds, selectedFoodIds)
            if (res.isSuccess()) {
                val nextFoods = res.data ?: emptyList()
                _uiState.update { state ->
                    state.copy(
                        isLoading = false,
                        currentRound = state.currentRound + 1,
                        foods = nextFoods,
                        selectedCurrentRound = emptySet(),
                        allReceivedFoodIds = state.allReceivedFoodIds + nextFoods.map { it.id }
                    )
                }
            } else {
                _uiState.update {
                    it.copy(isLoading = false, error = res.message)
                }
            }
        }
    }
}
