package com.plcoding.cryptotracker.crypto.presentation.coinlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plcoding.cryptotracker.core.domain.util.onError
import com.plcoding.cryptotracker.core.domain.util.onSuccess
import com.plcoding.cryptotracker.crypto.domain.CoinDataSource
import com.plcoding.cryptotracker.crypto.presentation.coindetail.chart.DataPoint
import com.plcoding.cryptotracker.crypto.presentation.models.CoinUi
import com.plcoding.cryptotracker.crypto.presentation.models.toCoinUi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

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

    private val _events = Channel<CoinListEvent>()
    val events = _events.receiveAsFlow()

    fun onAction(action: CoinListAction) {
        when (action) {
            is CoinListAction.OnCoinClick -> {
                selectCoin(action.coinUi)
            }
        }
    }

    private fun selectCoin(coinUi: CoinUi) {
        _state.update { it.copy(selectedCoin = coinUi) }

        viewModelScope.launch {
            coinDataSource.getCoinHistory(
                coinId = coinUi.id,
                start = ZonedDateTime.now().minusDays(defaultHistoryPeriodInDays),
                end = ZonedDateTime.now()
            ).onSuccess { coinHistory ->
                val dataPoints = coinHistory.map {
                    DataPoint(
                        x = it.dateTime.hour.toFloat(),
                        y = it.priceUsd.toFloat(),
                        xLabel = DateTimeFormatter.ofPattern("ha\nM/d").format(it.dateTime)
                    )
                }
                _state.update { it.copy(selectedCoin = it.selectedCoin?.copy(history = dataPoints)) }
            }.onError { error ->
                _events.send(CoinListEvent.Error(error))
            }
        }
    }

    private fun loadCoins() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            coinDataSource.getCoins().onSuccess { coins ->
                val coinsUi = coins.map { it.toCoinUi() }
                _state.update {
                    it.copy(isLoading = false, coins = coinsUi)
                }
            }.onError { error ->
                _state.update { it.copy(isLoading = false) }
                _events.send(CoinListEvent.Error(error))
            }
        }
    }

    companion object {
        private const val defaultHistoryPeriodInDays = 5L
    }
}