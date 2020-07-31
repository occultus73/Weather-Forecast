package io.github.occultus73.weatherforecast.model.network

import androidx.lifecycle.LiveData
import io.github.occultus73.weatherforecast.model.network.response.OneCallResponse

interface WeatherNetworkDataSource {
    val downloadedCurrentWeather: LiveData<OneCallResponse>


    suspend fun fetchCurrentWeather(
        latitude: Float,
        longitude: Float,
        units: String
    )
}