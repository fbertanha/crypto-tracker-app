package com.plcoding.cryptotracker.crypto.presentation.coinlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plcoding.cryptotracker.core.domain.util.onError
import com.plcoding.cryptotracker.core.domain.util.onSuccess
import com.plcoding.cryptotracker.crypto.domain.CoinDataSource
import com.plcoding.cryptotracker.crypto.presentation.models.toCoinUi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Created by felipebertanha on 12/November/2024
 */
class CoinListViewModel(
    private val coinDataSource: CoinDataSource // TODO Refactor to UseCase
) : ViewModel() {
    private val _state = MutableStateFlow(CoinListState())
    val state = _state.onStart { loadCoins() }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = CoinListState()
    )

    fun onAction(action: CoinListAction) {
        when (action) {
            is CoinListAction.OnCoinClick -> {

            }
        }
    }

    private suspend fun loadCoins() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            val coins = coinDataSource.getCoins().onSuccess { coins ->
                val coinsUi = coins.map { it.toCoinUi() }
                _state.update {
                    it.copy(isLoading = false, coins = coinsUi)
                }
            }.onError { error ->
                _state.update { it.copy(isLoading = false) }
            }
        }
    }
}