package com.plcoding.cryptotracker.crypto.presentation.coinlist.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.plcoding.cryptotracker.crypto.presentation.models.DisplayableNumber
import com.plcoding.cryptotracker.ui.theme.CryptoTrackerTheme
import com.plcoding.cryptotracker.ui.theme.greenBackground

/**
 * Created by felipebertanha on 11/November/2024
 */

@Composable
fun PriceChange(
    change: DisplayableNumber, modifier: Modifier = Modifier
) {
    val contentColor = if (change.isNegative) {
        MaterialTheme.colorScheme.onErrorContainer
    } else {
        Color.Green
    }

    val backGroundColor = if (change.isNegative) {
        MaterialTheme.colorScheme.errorContainer
    } else {
        greenBackground
    }

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(100f))
            .background(backGroundColor)
            .padding(horizontal = 8.dp), verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = if (change.isNegative) {
                Icons.Default.KeyboardArrowDown
            } else {
                Icons.Default.KeyboardArrowUp
            },
            contentDescription = "Coin icon",
            modifier = Modifier.size(20.dp),
            tint = contentColor
        )
        Text(
            text = change.formatted,
            color = contentColor,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )


    }
}

@PreviewLightDark
@Composable
private fun PriceChangePreview() {
    CryptoTrackerTheme {
        PriceChange(
            change = DisplayableNumber(
                value = 29.07, formatted = "29.07 %"
            )
        )
    }
}
