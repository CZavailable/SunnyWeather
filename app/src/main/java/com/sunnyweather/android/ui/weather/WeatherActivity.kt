package com.sunnyweather.android.ui.weather

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.sunnyweather.android.R
import com.sunnyweather.android.logic.model.Weather
import com.sunnyweather.android.logic.model.getSky
import java.text.SimpleDateFormat
import java.util.Locale

class WeatherActivity : AppCompatActivity() {

    val viewModel by lazy { ViewModelProvider(this).get(WeatherViewModel::class.java) }

    private lateinit var placeName: TextView
    private lateinit var currentTemp: TextView
    private lateinit var currentSky: TextView
    private lateinit var currentAQI: TextView
    private lateinit var nowLayout: View
    private lateinit var forecastLayout: LinearLayout
    private lateinit var coldRiskText: TextView
    private lateinit var dressingText: TextView
    private lateinit var ultravioletText: TextView
    private lateinit var carWashingText: TextView
    private lateinit var weatherLayout: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)

        placeName = findViewById(R.id.placeName)
        currentTemp = findViewById(R.id.currentTemp)
        currentSky = findViewById(R.id.currentSky)
        currentAQI = findViewById(R.id.currentAQI)
        nowLayout = findViewById(R.id.nowLayout)
        forecastLayout = findViewById(R.id.forecastLayout)
        coldRiskText = findViewById(R.id.coldRiskText)
        dressingText = findViewById(R.id.dressingText)
        ultravioletText = findViewById(R.id.ultravioletText)
        carWashingText = findViewById(R.id.carWashingText)
        weatherLayout = findViewById(R.id.weatherLayout)

        if (viewModel.locationLng.isEmpty()) {
            viewModel.locationLng = intent.getStringExtra("location_lng") ?: ""
        }
        if (viewModel.locationLat.isEmpty()) {
            viewModel.locationLat = intent.getStringExtra("location_lat") ?: ""
        }
        if (viewModel.placeName.isEmpty()) {
            viewModel.placeName = intent.getStringExtra("place_name") ?: ""
        }

//        viewModel.weatherLiveData.observe(this, Observer { result ->
//            val weather = result.getOrNull()
//            if (weather != null) {
//                showWeatherInfo(weather)
//            } else {
//                Toast.makeText(this, "无法成功获取天气信息", Toast.LENGTH_SHORT).show()
//                result.exceptionOrNull()?.printStackTrace()
//            }
//        })
        viewModel.weatherLiveData.observe(this, Observer { result ->
            val weather = result.getOrNull()
//            if (weather != null) {
//                showWeatherInfo(weather)
//            } else {
//                val e = result.exceptionOrNull()
//                Toast.makeText(this, "获取天气失败: ${e?.javaClass?.simpleName} ${e?.message}", Toast.LENGTH_LONG).show()
//                e?.printStackTrace()
//            }
            if (weather != null) {
                try {
                    showWeatherInfo(weather)
                } catch (e: Exception) {
                    Toast.makeText(
                        this,
                        "showWeatherInfo崩溃: ${e.javaClass.simpleName} ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                    e.printStackTrace()
                }
            } else {
                val e = result.exceptionOrNull()
                Toast.makeText(
                    this,
                    "获取天气失败: ${e?.javaClass?.simpleName} ${e?.message}",
                    Toast.LENGTH_LONG
                ).show()
                e?.printStackTrace()
            }
        })

        viewModel.refreshWeather(viewModel.locationLng, viewModel.locationLat)
    }

    private fun showWeatherInfo(weather: Weather) {
        placeName.text = viewModel.placeName

        val realtime = weather.realtime
        val daily = weather.daily

        val currentTempText = "${realtime.temperature.toInt()} ℃"
        currentTemp.text = currentTempText
        currentSky.text = getSky(realtime.skycon).info

        val currentPM25Text = "空气指数 ${realtime.airQuality.aqi.chn.toInt()}"
        currentAQI.text = currentPM25Text

        nowLayout.setBackgroundResource(getSky(realtime.skycon).bg)

        forecastLayout.removeAllViews()
        val days = daily.skycon.size

        for (i in 0 until days) {
            val skycon = daily.skycon[i]
            val temperature = daily.temperature[i]

            val view = LayoutInflater.from(this)
                .inflate(R.layout.forecast_item, forecastLayout, false)

            val dateInfo = view.findViewById<TextView>(R.id.dateInfo)
            val skyIcon = view.findViewById<ImageView>(R.id.skyIcon)
            val skyInfo = view.findViewById<TextView>(R.id.skyInfo)
            val temperatureInfo = view.findViewById<TextView>(R.id.temperatureInfo)

            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            dateInfo.text = simpleDateFormat.format(skycon.date)

            val sky = getSky(skycon.value)
            skyIcon.setImageResource(sky.icon)
            skyInfo.text = sky.info

            val tempText = "${temperature.min.toInt()} ~ ${temperature.max.toInt()} ℃"
            temperatureInfo.text = tempText

            forecastLayout.addView(view)
        }

        val lifeIndex = daily.lifeIndex
//        coldRiskText.text = lifeIndex.coldRisk[0].desc
//        dressingText.text = lifeIndex.dressing[0].desc
//        ultravioletText.text = lifeIndex.ultraviolet[0].desc
//        carWashingText.text = lifeIndex.carWashing[0].desc
        coldRiskText.text = lifeIndex.coldRisk?.firstOrNull()?.desc ?: "暂无数据"
        dressingText.text = lifeIndex.dressing?.firstOrNull()?.desc ?: "暂无数据"
        ultravioletText.text = lifeIndex.ultraviolet?.firstOrNull()?.desc ?: "暂无数据"
        carWashingText.text = lifeIndex.carWashing?.firstOrNull()?.desc ?: "暂无数据"

        weatherLayout.visibility = View.VISIBLE
    }
}