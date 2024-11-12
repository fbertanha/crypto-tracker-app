package com.plcoding.cryptotracker.di

import com.plcoding.cryptotracker.core.data.networking.HttpClientFactory
import com.plcoding.cryptotracker.crypto.data.networking.RemoteCoinDataSource
import com.plcoding.cryptotracker.crypto.domain.CoinDataSource
import com.plcoding.cryptotracker.crypto.presentation.coinlist.CoinListViewModel
import io.ktor.client.engine.cio.CIO
import org.koin.androidx.viewmodel.dsl.viewModelOf

import org.koin.dsl.module

/**
 * Created by felipebertanha on 12/November/2024
 */

val appModule = module {
    single { HttpClientFactory.create(engine = CIO.create()) }
    single<CoinDataSource> { RemoteCoinDataSource(httpClient = get()) }

    viewModelOf(::CoinListViewModel)
}