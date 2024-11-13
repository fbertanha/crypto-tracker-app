package com.plcoding.cryptotracker.crypto.data.mappers

import com.plcoding.cryptotracker.core.data.extensions.toZonedDateTime
import com.plcoding.cryptotracker.crypto.data.networking.dto.CoinDto
import com.plcoding.cryptotracker.crypto.data.networking.dto.CoinHistoryDto
import com.plcoding.cryptotracker.crypto.domain.models.Coin
import com.plcoding.cryptotracker.crypto.domain.models.CoinHistory

/**
 * Created by felipebertanha on 12/November/2024
 */

fun CoinDto.toCoin(): Coin {
    return Coin(
        id = id,
        rank = rank,
        name = name,
        symbol = symbol,
        marketCapUsd = marketCapUsd,
        priceUsd = priceUsd,
        changePercent24Hr = changePercent24Hr
    )
}

fun CoinHistoryDto.toCoinHistory(): CoinHistory {
    return CoinHistory(
        priceUsd = priceUsd, dateTime = time.toZonedDateTime()
    )
}