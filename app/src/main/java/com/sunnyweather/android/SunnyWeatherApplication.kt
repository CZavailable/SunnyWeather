package com.sunnyweather.android
import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

class SunnyWeatherApplication : Application() {
    companion object {
        const val TOKEN = "nZ8Iau75UVG0cwO3"
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
    }
}