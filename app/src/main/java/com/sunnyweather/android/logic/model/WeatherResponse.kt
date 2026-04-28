package com.sunnyweather.android.logic.model

data class WeatherResponse(val status: String, val result: Result) {
    data class Result(
        val realtime: RealtimeResponse.Realtime,
        val daily: DailyResponse.Daily
    )
}