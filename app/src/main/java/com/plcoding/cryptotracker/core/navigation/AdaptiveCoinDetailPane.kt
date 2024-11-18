@file:OptIn(ExperimentalMaterial3AdaptiveApi::class)

package com.plcoding.cryptotracker.core.navigation

import android.widget.Toast
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.NavigableListDetailPaneScaffold
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.plcoding.cryptotracker.core.presentation.util.ObserveAsEvents
import com.plcoding.cryptotracker.core.presentation.util.toString
import com.plcoding.cryptotracker.crypto.presentation.coindetail.CoinDetailScreen
import com.plcoding.cryptotracker.crypto.presentation.coinlist.CoinListAction
import com.plcoding.cryptotracker.crypto.presentation.coinlist.CoinListEvent
import com.plcoding.cryptotracker.crypto.presentation.coinlist.CoinListScreen
import com.plcoding.cryptotracker.crypto.presentation.coinlist.CoinListViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun AdaptiveCoinDetailPane(
    viewModel: CoinListViewModel = koinViewModel(), modifier: Modifier = Modifier
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val events = viewModel.events

    val context = LocalContext.current

    ObserveAsEvents(events = events) { event ->
        when (event) {
            is CoinListEvent.Error -> {
                Toast.makeText(
                    context, event.error.toString(context), Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    val navigator = rememberListDetailPaneScaffoldNavigator<Any>()
    NavigableListDetailPaneScaffold(navigator = navigator, listPane = {
        AnimatedPane {
            CoinListScreen(
                state = state,
                onAction = { action ->
                    viewModel.onAction(action)
                    when (action) {
                        is CoinListAction.OnCoinClick -> {
                            navigator.navigateTo(pane = ListDetailPaneScaffoldRole.Detail)
                        }
                    }
                },
            )
        }
    }, detailPane = {
        AnimatedPane {
            CoinDetailScreen(
                state = state
            )
        }

    }, modifier = modifier)
}