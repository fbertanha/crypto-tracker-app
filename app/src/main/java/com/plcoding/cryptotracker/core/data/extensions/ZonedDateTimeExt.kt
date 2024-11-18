package com.plcoding.cryptotracker.core.data.extensions

import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime

fun ZonedDateTime.toMillis(zoneId: ZoneId = ZoneId.of("UTC")): Long {
    return this.withZoneSameLocal(zoneId).toInstant().toEpochMilli()
}

fun Long.toZonedDateTime(zoneId: ZoneId = ZoneId.systemDefault()): ZonedDateTime {
    return Instant.ofEpochMilli(this).atZone(zoneId)
}