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
    val cards: List<Food> = emptyList(),
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
    private var activeRound: Int = 0
    private val allReceivedFoodIds = linkedSetOf<Int>()
    private val allSelectedFoodIds = linkedSetOf<Int>()
    private var lastFoodIds: List<Int> = emptyList()
    private var lastSelectedFoodIds: List<Int> = emptyList()

    fun start(token: String) {
        if (authToken != null) return
        authToken = token
        requestRound(foodIds = emptyList(), selectedFoodIds = emptyList())
    }

    fun onLike() {
        consumeTopCard(isSelected = true)
    }

    fun onSkip() {
        consumeTopCard(isSelected = false)
    }

    fun onDislike() {
        consumeTopCard(isSelected = false)
    }

    fun retry() {
        if (_uiState.value.isLoading || authToken == null || _uiState.value.isCompleted) return
        requestRound(lastFoodIds, lastSelectedFoodIds)
    }

    private fun consumeTopCard(isSelected: Boolean) {
        val topCard = _uiState.value.cards.firstOrNull() ?: return
        if (_uiState.value.isLoading || _uiState.value.isCompleted) return

        allReceivedFoodIds.add(topCard.id)
        if (isSelected) {
            allSelectedFoodIds.add(topCard.id)
        }

        _uiState.update {
            it.copy(cards = it.cards.drop(1), error = null)
        }

        if (_uiState.value.cards.isNotEmpty()) {
            return
        }

        if (activeRound >= MAX_ROUNDS) {
            completeOnboarding()
        } else {
            requestRound(
                foodIds = allReceivedFoodIds.toList(),
                selectedFoodIds = allSelectedFoodIds.toList()
            )
        }
    }

    private fun requestRound(foodIds: List<Int>, selectedFoodIds: List<Int>) {
        if (_uiState.value.isLoading || _uiState.value.isCompleted) return

        val token = authToken ?: return
        lastFoodIds = foodIds
        lastSelectedFoodIds = selectedFoodIds

        _uiState.update {
            it.copy(isLoading = true, error = null)
        }

        viewModelScope.launch {
            val response = foodRepository.consecutiveSuggest(
                token = token,
                foodIds = foodIds,
                selectedFoodIds = selectedFoodIds
            )
            if (response.isSuccess()) {
                activeRound += 1
                val nextCards = response.data ?: emptyList()
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = null,
                        currentRound = activeRound,
                        cards = nextCards
                    )
                }
                if (nextCards.isEmpty()) {
                    if (activeRound >= MAX_ROUNDS) {
                        completeOnboarding()
                    } else {
                        _uiState.update {
                            it.copy(error = "本轮未返回菜品，请重试。")
                        }
                    }
                }
            } else {
                _uiState.update {
                    it.copy(isLoading = false, error = response.message)
                }
            }
        }
    }

    private fun completeOnboarding() {
        _uiState.update {
            it.copy(isCompleted = true)
        }
        viewModelScope.launch {
            localStoreRepository.markOnboardingCompleted()
        }
    }
}
