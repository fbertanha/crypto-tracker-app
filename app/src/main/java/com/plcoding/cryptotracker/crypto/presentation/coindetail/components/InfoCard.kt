package com.plcoding.cryptotracker.crypto.presentation.coindetail.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.plcoding.cryptotracker.R
import com.plcoding.cryptotracker.ui.theme.CryptoTrackerTheme

@Composable
fun InfoCard(
    icon: ImageVector,
    title: String,
    formattedText: String,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    modifier: Modifier = Modifier
) {
    val defaultTextStyle: TextStyle = LocalTextStyle.current.copy(
        textAlign = TextAlign.Center, fontSize = 18.sp, color = contentColor
    )
    Card(
        modifier = modifier
            .padding(16.dp)
            .shadow(
                elevation = 16.dp,
                shape = RectangleShape,
                ambientColor = MaterialTheme.colorScheme.primary,
                spotColor = MaterialTheme.colorScheme.primary
            ), shape = RectangleShape, border = BorderStroke(
            width = 1.dp, color = MaterialTheme.colorScheme.primary
        ), colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer, contentColor = contentColor
        )
    ) {
        AnimatedContent(
            targetState = icon,
            label = "CoinIconAnimation",
            modifier = modifier.align(Alignment.CenterHorizontally)
        ) {
            Icon(
                imageVector = it,
                contentDescription = "$title icon",
                modifier = Modifier
                    .size(75.dp)
                    .padding(top = 16.dp),
                tint = contentColor
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        AnimatedContent(
            targetState = formattedText,
            label = "FormattedTextAnimation",
            modifier = modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(
                it, style = defaultTextStyle, modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            title,
            textAlign = TextAlign.Center,
            fontSize = 12.sp,
            fontWeight = FontWeight.Light,
            color = contentColor,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(horizontal = 16.dp)
                .padding(bottom = 16.dp)
        )


    }

}

@PreviewLightDark
@Composable
private fun InfoCardPreview() {

    CryptoTrackerTheme {
        InfoCard(
            icon = ImageVector.vectorResource(R.drawable.dollar),
            title = "Price",
            formattedText = "$7,919.95"
        )
    }
}