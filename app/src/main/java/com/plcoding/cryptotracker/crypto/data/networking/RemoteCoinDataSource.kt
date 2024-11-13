package com.plcoding.cryptotracker.crypto.data.networking

import com.plcoding.cryptotracker.core.data.extensions.toMillis
import com.plcoding.cryptotracker.core.data.networking.constructUrl
import com.plcoding.cryptotracker.core.data.networking.safeCall
import com.plcoding.cryptotracker.core.domain.util.NetworkError
import com.plcoding.cryptotracker.core.domain.util.Result
import com.plcoding.cryptotracker.core.domain.util.map
import com.plcoding.cryptotracker.crypto.data.mappers.toCoin
import com.plcoding.cryptotracker.crypto.data.mappers.toCoinHistory
import com.plcoding.cryptotracker.crypto.data.networking.dto.CoinHistoryResponseDto
import com.plcoding.cryptotracker.crypto.data.networking.dto.CoinsResponseDto
import com.plcoding.cryptotracker.crypto.domain.CoinDataSource
import com.plcoding.cryptotracker.crypto.domain.models.Coin
import com.plcoding.cryptotracker.crypto.domain.models.CoinHistory
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import java.time.ZonedDateTime

/**
 * Created by felipebertanha on 12/November/2024
 */
class RemoteCoinDataSource(
    private val httpClient: HttpClient
) : CoinDataSource {
    override suspend fun getCoins(): Result<List<Coin>, NetworkError> {
        return safeCall<CoinsResponseDto> {
            httpClient.get(
                urlString = constructUrl("/assets")
            )
        }.map { response ->
            response.data.map { it.toCoin() }
        }
    }

    override suspend fun getCoinHistory(
        coinId: String, start: ZonedDateTime, end: ZonedDateTime
    ): Result<List<CoinHistory>, NetworkError> {
        return safeCall<CoinHistoryResponseDto> {
            httpClient.get(
                urlString = constructUrl("/assets/${coinId}/history")
            ) {
                parameter("interval", "h6")
                parameter("start", start.toMillis())
                parameter("end", end.toMillis())
            }
        }.map { response ->
            response.data.map { it.toCoinHistory() }
        }
    }
}