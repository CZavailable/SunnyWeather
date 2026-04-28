package com.sunnyweather.android.logic.dao

import android.content.Context
import androidx.core.content.edit
import com.google.gson.Gson
import com.sunnyweather.android.SunnyWeatherApplication
import com.sunnyweather.android.logic.model.Place

object PlaceDao {

    fun savePlace(place: Place) {
        sharedPreferences().edit {
            putString("place", Gson().toJson(place))
        }
    }

    fun getSavedPlace(): Place? {
        val placeJson = sharedPreferences().getString("place", null) ?: return null
        return try {
            Gson().fromJson(placeJson, Place::class.java)
        } catch (e: Exception) {
            null
        }
    }

    fun isPlaceSaved(): Boolean {
        val placeJson = sharedPreferences().getString("place", null)
        return !placeJson.isNullOrEmpty()
    }

    private fun sharedPreferences() =
        SunnyWeatherApplication.context.getSharedPreferences(
            "sunny_weather",
            Context.MODE_PRIVATE
        )
}