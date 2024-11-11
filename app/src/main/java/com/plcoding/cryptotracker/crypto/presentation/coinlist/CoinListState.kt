package com.plcoding.cryptotracker.crypto.presentation.coinlist

import androidx.compose.runtime.Immutable
import com.plcoding.cryptotracker.crypto.presentation.models.CoinUi

/**
 * Created by felipebertanha on 11/November/2024
 */
@Immutable
data class CoinListState(
    val isLoading: Boolean = false,
    val coins: List<CoinUi> = emptyList(),
    val selectedCoin: CoinUi? = null
)
