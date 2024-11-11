package com.plcoding.cryptotracker.crypto.domain.models

/**
 * Created by felipebertanha on 11/November/2024
 */
data class Coin(
    val id: String,
    val rank: Int,
    val name: String,
    val symbol: String,
    val marketCapUsd: Double,
    val priceUsd: Double,
    val changePercent24Hr: Double
)
