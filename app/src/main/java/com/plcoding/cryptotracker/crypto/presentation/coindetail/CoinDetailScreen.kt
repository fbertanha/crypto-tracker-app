@file:OptIn(ExperimentalLayoutApi::class)

package com.plcoding.cryptotracker.crypto.presentation.coindetail

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.plcoding.cryptotracker.R
import com.plcoding.cryptotracker.crypto.presentation.coindetail.components.InfoCard
import com.plcoding.cryptotracker.crypto.presentation.coinlist.CoinListState
import com.plcoding.cryptotracker.crypto.presentation.coinlist.components.previewCoin
import com.plcoding.cryptotracker.ui.theme.CryptoTrackerTheme
import com.plcoding.cryptotracker.ui.theme.greenBackground

@Composable
fun CoinDetailScreen(
    state: CoinListState, modifier: Modifier = Modifier
) {
    val contentColor = if (isSystemInDarkTheme()) {
        Color.White
    } else {
        Color.Black
    }

    if (state.isLoading) {
        Box(
            modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else if (state.selectedCoin != null) {
        val coin = state.selectedCoin

        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(coin.iconRes),
                contentDescription = "${coin.name} icon",
                modifier = modifier.size(100.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Text(
                text = coin.name,
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = contentColor
            )

            Text(
                text = coin.symbol,
                fontSize = 20.sp,
                fontWeight = FontWeight.Light,
                textAlign = TextAlign.Center,
                color = contentColor
            )

            FlowRow(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center
            ) {
                InfoCard(
                    icon = ImageVector.vectorResource(R.drawable.stock),
                    title = stringResource(R.string.market_cap),
                    formattedText = coin.marketCapUsd.formatted
                )

                InfoCard(
                    icon = ImageVector.vectorResource(R.drawable.dollar),
                    title = stringResource(R.string.price),
                    formattedText = coin.priceUsd.formatted
                )

                val absoluteChangeContentColor = if (coin.absoluteChangeAmountUsd.isNegative) {
                    MaterialTheme.colorScheme.error
                } else {
                    if (isSystemInDarkTheme()) Color.Green else greenBackground
                }

                val absoluteChangeIcon = if (coin.absoluteChangeAmountUsd.isNegative) {
                    ImageVector.vectorResource(R.drawable.trending_down)
                } else {
                    ImageVector.vectorResource(R.drawable.trending)
                }

                InfoCard(
                    icon = absoluteChangeIcon,
                    title = stringResource(R.string.change_last_24h),
                    formattedText = coin.absoluteChangeAmountUsd.formatted,
                    contentColor = absoluteChangeContentColor
                )

            }
        }
    }

}

@PreviewLightDark
@Composable
private fun CoinDetailScreenPreview() {
    CryptoTrackerTheme {
        CoinDetailScreen(
            state = CoinListState(selectedCoin = previewCoin),
            modifier = Modifier.background(MaterialTheme.colorScheme.background)
        )
    }
}