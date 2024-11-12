package com.plcoding.cryptotracker.crypto.domain

import com.plcoding.cryptotracker.core.domain.util.NetworkError
import com.plcoding.cryptotracker.core.domain.util.Result
import com.plcoding.cryptotracker.crypto.domain.models.Coin

/**
 * Created by felipebertanha on 12/November/2024
 */
interface CoinDataSource {
    suspend fun getCoins(): Result<List<Coin>, NetworkError>
}
