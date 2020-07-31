package io.github.occultus73.weatherforecast.model.network

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.github.occultus73.weatherforecast.internal.NoConnectivityException
import io.github.occultus73.weatherforecast.model.network.response.OneCallResponse

class WeatherNetworkDataSourceImpl(
    private val openWeatherMapApiService: OpenWeatherMapApiService,
    private val apixuWeatherApiService: ApixuWeatherApiService
) : WeatherNetworkDataSource {
    // new weather downloaded from OpenWeatherMap
    private val _downloadedOneCallWeather = MutableLiveData<OneCallResponse>()
    override val downloadedCurrentWeather: LiveData<OneCallResponse>
        get() = _downloadedOneCallWeather

    override suspend fun fetchCurrentWeather(latitude: Float, longitude: Float, units: String) {
        try {
            val fetchedCurrentOpenWeather = openWeatherMapApiService
                .getCurrentWeather(latitude, longitude, units)
                .await()


            val fetchedCurrentApixuWeather = apixuWeatherApiService
                .getCurrentWeather("$latitude,$longitude")
                .await()

            //tack one shamelessly onto the other
            fetchedCurrentOpenWeather.currentWeatherEntry.apixuWeatherResponse = fetchedCurrentApixuWeather

            _downloadedOneCallWeather.postValue(fetchedCurrentOpenWeather)

        } catch (e: NoConnectivityException) {
            Log.e("Connectivity", "No internet connection.", e)
        }
    }
}