package com.plcoding.cryptotracker.core.domain.util

/**
 * Created by felipebertanha on 11/November/2024
 */
enum class NetworkError : Error {
    REQUEST_TIMEOUT,
    TOO_MANY_REQUESTS,
    NO_INTERNET,
    SERVER_ERROR,
    SERIALIZATION,
    UNKNOWN
}
