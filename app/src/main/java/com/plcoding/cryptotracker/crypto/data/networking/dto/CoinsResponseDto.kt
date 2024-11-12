package com.plcoding.cryptotracker.crypto.data.networking.dto

import kotlinx.serialization.Serializable

/**
 * Created by felipebertanha on 12/November/2024
 */
@Serializable
data class CoinsResponseDto(
    val data: List<CoinDto>
)
