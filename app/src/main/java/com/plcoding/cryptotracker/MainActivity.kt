package com.plcoding.cryptotracker

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.plcoding.cryptotracker.core.presentation.util.ObserveAsEvents
import com.plcoding.cryptotracker.core.presentation.util.toString
import com.plcoding.cryptotracker.crypto.presentation.coindetail.CoinDetailScreen
import com.plcoding.cryptotracker.crypto.presentation.coinlist.CoinListEvent
import com.plcoding.cryptotracker.crypto.presentation.coinlist.CoinListScreen
import com.plcoding.cryptotracker.crypto.presentation.coinlist.CoinListViewModel
import com.plcoding.cryptotracker.ui.theme.CryptoTrackerTheme
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CryptoTrackerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val context = LocalContext.current
                    val viewModel = koinViewModel<CoinListViewModel>()
                    val state by viewModel.state.collectAsStateWithLifecycle()
                    val events = viewModel.events

                    ObserveAsEvents(events = events) { event ->
                        when (event) {
                            is CoinListEvent.Error -> {
                                Toast.makeText(
                                    context, event.error.toString(context), Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }

                    when {
                        state.selectedCoin != null -> {
                            CoinDetailScreen(
                                state = state, modifier = Modifier.padding(innerPadding)
                            )
                        }

                        else -> {
                            CoinListScreen(
                                state = state,
                                onAction = { viewModel.onAction(it) },
                                modifier = Modifier.padding(innerPadding)
                            )
                        }
                    }
                }
            }
        }
    }
}
