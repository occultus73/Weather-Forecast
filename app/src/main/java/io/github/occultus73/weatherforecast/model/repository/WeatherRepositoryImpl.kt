package io.github.occultus73.weatherforecast.model.repository

import androidx.lifecycle.LiveData
import io.github.occultus73.weatherforecast.model.db.CurrentWeatherDao
import io.github.occultus73.weatherforecast.model.db.entity.CurrentWeatherEntry
import io.github.occultus73.weatherforecast.model.network.WeatherNetworkDataSource
import io.github.occultus73.weatherforecast.model.network.response.Location
import io.github.occultus73.weatherforecast.model.network.response.OneCallResponse
import io.github.occultus73.weatherforecast.model.provider.LocationProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.threeten.bp.ZonedDateTime

class WeatherRepositoryImpl(
    private val currentWeatherDao: CurrentWeatherDao,
    private val weatherNetworkDataSource: WeatherNetworkDataSource,
    private val locationProvider: LocationProvider
) : WeatherRepository {
    private var _currentWeather: LiveData<CurrentWeatherEntry> = currentWeatherDao.getWeather()

    init {
        weatherNetworkDataSource.downloadedCurrentWeather.observeForever { newCurrentWeather ->
            persistFetchedCurrentWeather(newCurrentWeather)
        }
    }

    override suspend fun getCurrentWeather(units: String): LiveData<CurrentWeatherEntry> {
        // update weather cached in database by new OpenWeatherMap API OneCall if 1 hour ago
        if (isFetchedCurrentNeeded(ZonedDateTime.now().minusHours(1)))
            weatherNetworkDataSource.fetchCurrentWeather(units)

        return _currentWeather
    }

    private suspend fun isFetchedCurrentNeeded(thresholdTimeAgo: ZonedDateTime): Boolean {
        val lastWeatherRequest = _currentWeather.value?.apixuWeatherResponse?.location
        return thresholdTimeAgo.isAfter(lastWeatherRequest?.zonedDateTime ?: return true)
                || locationProvider.hasLocationChanged(lastWeatherRequest)
    }

    private fun persistFetchedCurrentWeather(fetchedWeather: OneCallResponse) {
        GlobalScope.launch(Dispatchers.IO) {
            //insert fetchedWeather into database
            currentWeatherDao.upsert(fetchedWeather.currentWeatherEntry)

            // get current weather cached from the database
            _currentWeather = currentWeatherDao.getWeather()
        }
    }
}