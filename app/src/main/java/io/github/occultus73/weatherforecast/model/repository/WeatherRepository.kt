package io.github.occultus73.weatherforecast.model.repository

import androidx.lifecycle.LiveData
import io.github.occultus73.weatherforecast.model.db.entity.CurrentWeatherEntry

interface WeatherRepository {
    suspend fun getCurrentWeather(units: String): LiveData<CurrentWeatherEntry>
}