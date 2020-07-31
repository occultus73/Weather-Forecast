package io.github.occultus73.weatherforecast.model.repository

import androidx.lifecycle.LiveData
import io.github.occultus73.weatherforecast.model.db.entity.CurrentWeatherEntry

interface WeatherRepository {
    suspend fun getCurrentWeather(units: String, lat: Float = 51.283791f, lon: Float = 0.0831033f): LiveData<CurrentWeatherEntry>
}