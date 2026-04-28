package com.sunnyweather.android.logic.network

import com.sunnyweather.android.SunnyWeatherApplication
import com.sunnyweather.android.logic.model.DailyResponse
import com.sunnyweather.android.logic.model.RealtimeResponse
import com.sunnyweather.android.logic.model.WeatherResponse
import retrofit2.http.GET
import retrofit2.Call
import retrofit2.http.Path

interface WeatherService {
//    @GET("v2.5/${SunnyWeatherApplication.TOKEN}/{lng},{lat}/realtime.json")
//    fun getRealtimeWeather(@Path("lng") lng: String, @Path("lat") lat: String):
//        Call<RealtimeResponse>
//
//    @GET("v2.5/${SunnyWeatherApplication.TOKEN}/{lng},{lat}/daily.json")
//    fun getDailyWeather(@Path("lng") lng: String, @Path("lat") lat: String):
//        Call<DailyResponse>
    @GET("v2.5/${SunnyWeatherApplication.TOKEN}/{lng},{lat}/weather.json?lang=zh_CN&dailysteps=5")
    fun getWeather(@Path("lng") lng: String, @Path("lat") lat: String): Call<WeatherResponse>

}