package com.plcoding.cryptotracker.crypto.presentation.models

import android.icu.text.NumberFormat
import androidx.annotation.DrawableRes
import com.plcoding.cryptotracker.crypto.domain.models.Coin
import com.plcoding.cryptotracker.util.getDrawableIdForCoin
import java.util.Locale


/**
 * Created by felipebertanha on 11/November/2024
 */
data class CoinUi(
    val id: String,
    val rank: Int,
    val name: String,
    val symbol: String,
    val marketCapUsd: DisplayableNumber,
    val priceUsd: DisplayableNumber,
    val changePercent24Hr: DisplayableNumber,
    @DrawableRes val iconRes: Int
)

data class DisplayableNumber(
    val value: Double,
    val formatted: String,
) {
    val isNegative get() = value < 0
}

fun Coin.toCoinUi(): CoinUi {
    return CoinUi(
        id = id,
        name = name,
        symbol = symbol,
        rank = rank,
        priceUsd = priceUsd.toDisplayableCurrency(locale = Locale.US),
        marketCapUsd = marketCapUsd.toDisplayableCurrency(locale = Locale.US),
        changePercent24Hr = changePercent24Hr.toDisplayableNumber(suffix = "%"),
        iconRes = getDrawableIdForCoin(symbol)
    )
}

fun Double.toDisplayableCurrency(locale: Locale = Locale.getDefault()): DisplayableNumber {
    val usdCurrencyFormatter = NumberFormat.getCurrencyInstance(locale).apply {
        minimumFractionDigits = 2
        maximumFractionDigits = 2
    }
    return DisplayableNumber(
        value = this, formatted = usdCurrencyFormatter.format(this)
    )
}

fun Double.toDisplayableNumber(prefix: String = "", suffix: String = ""): DisplayableNumber {
    val usdCurrencyFormatter = NumberFormat.getNumberInstance(Locale.getDefault()).apply {
        minimumFractionDigits = 2
        maximumFractionDigits = 2
    }
    return DisplayableNumber(
        value = this, formatted = "$prefix ${usdCurrencyFormatter.format(this)} $suffix"
    )
}
