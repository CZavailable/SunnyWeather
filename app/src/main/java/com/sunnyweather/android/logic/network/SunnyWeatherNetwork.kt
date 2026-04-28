package com.sunnyweather.android.logic.network

import com.sunnyweather.android.logic.model.Weather
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object SunnyWeatherNetwork {
    private val placeService = ServiceCreator.create<PlaceService>()

    private val weatherService = ServiceCreator.create(WeatherService::class.java)

    suspend fun searchPlaces(query: String) = placeService.searchPlaces(query).await()

//    suspend fun getDailyWeather(lng : String, lat : String) =
//        weatherService.getDailyWeather(lng, lat).await()
//
//    suspend fun getRealtimeWeather(lng : String, lat : String) =
//        weatherService.getRealtimeWeather(lng, lat).await()

    suspend fun getWeather(lng: String, lat: String) =
        weatherService.getWeather(lng, lat).await()
    private suspend fun <T> Call<T>.await(): T {
        return suspendCoroutine { continuation ->
            enqueue(object : Callback<T> {
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    val body = response.body()
                    if (response.isSuccessful && body != null) {
                        continuation.resume(body)
                    } else {
                        val errorText = try {
                            response.errorBody()?.string()
                        } catch (e: Exception) {
                            null
                        }
                        continuation.resumeWithException(
                            RuntimeException(
                                "HTTP ${response.code()} ${response.message()}, errorBody=$errorText"
                            )
                        )
                    }
                }

                override fun onFailure(call: Call<T>, t: Throwable) {
                    continuation.resumeWithException(t)
                }
            })
        }
    }
}