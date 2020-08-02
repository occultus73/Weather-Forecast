package io.github.occultus73.weatherforecast.model.network

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.github.occultus73.weatherforecast.internal.NoConnectivityException
import io.github.occultus73.weatherforecast.model.network.response.OneCallResponse
import io.github.occultus73.weatherforecast.model.provider.LocationProvider

class WeatherNetworkDataSourceImpl(
    private val openWeatherMapApiService: OpenWeatherMapApiService,
    private val apixuWeatherApiService: ApixuWeatherApiService,
    private val locationProvider: LocationProvider
) : WeatherNetworkDataSource {
    // new weather downloaded from OpenWeatherMap
    private val _downloadedOneCallWeather = MutableLiveData<OneCallResponse>()
    override val downloadedCurrentWeather: LiveData<OneCallResponse>
        get() = _downloadedOneCallWeather

    override suspend fun fetchCurrentWeather(units: String) {
        val preferred = locationProvider.getPreferredLocation()

        try {
            val fetchedCurrentOpenWeather = openWeatherMapApiService
                .getCurrentWeather(preferred.latitude, preferred.longitude, units)
                .await()


            val fetchedCurrentApixuWeather = apixuWeatherApiService
                .getCurrentWeather("${preferred.latitude},${preferred.longitude}")
                .await()

            //tack one shamelessly onto the other
            fetchedCurrentOpenWeather.currentWeatherEntry.apixuWeatherResponse = fetchedCurrentApixuWeather

            _downloadedOneCallWeather.postValue(fetchedCurrentOpenWeather)

        } catch (e: NoConnectivityException) {
            Log.e("Connectivity", "No internet connection.", e)
        }
    }
}