package com.plcoding.cryptotracker.crypto.presentation.coinlist

import com.plcoding.cryptotracker.crypto.presentation.models.CoinUi

/**
 * Created by felipebertanha on 12/November/2024
 */
sealed interface CoinListAction {
    data class OnCoinClick(val coinUi: CoinUi) : CoinListAction
}