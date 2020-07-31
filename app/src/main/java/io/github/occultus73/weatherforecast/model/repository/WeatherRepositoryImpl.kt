package io.github.occultus73.weatherforecast.model.repository

import androidx.lifecycle.LiveData
import io.github.occultus73.weatherforecast.model.db.CurrentWeatherDao
import io.github.occultus73.weatherforecast.model.db.entity.CurrentWeatherEntry
import io.github.occultus73.weatherforecast.model.network.WeatherNetworkDataSource
import io.github.occultus73.weatherforecast.model.network.response.OneCallResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.threeten.bp.ZonedDateTime

class WeatherRepositoryImpl(
    private val currentWeatherDao: CurrentWeatherDao,
    private val weatherNetworkDataSource: WeatherNetworkDataSource
) : WeatherRepository {

    init {
        weatherNetworkDataSource.downloadedCurrentWeather.observeForever { newCurrentWeather ->
            persistFetchedCurrentWeather(newCurrentWeather)
        }
    }

    override suspend fun getCurrentWeather(units: String, lat: Float, lon: Float): LiveData<CurrentWeatherEntry> {

        // update weather cached in database by new OpenWeatherMap API OneCall if 30 minutes old
        if (isFetchedCurrentNeeded(ZonedDateTime.now().minusHours(1)))
            weatherNetworkDataSource.fetchCurrentWeather(lat, lon, units)

        // get current weather cached from the database
        return withContext(Dispatchers.IO) {
            return@withContext currentWeatherDao.getWeather()
        }
    }

    private fun isFetchedCurrentNeeded(lastFetchTime: ZonedDateTime): Boolean {
        val thirtyMinutesAgo = ZonedDateTime.now().minusMinutes(30)
        return lastFetchTime.isBefore(thirtyMinutesAgo)
    }

    private fun persistFetchedCurrentWeather(fetchedWeather: OneCallResponse) {
        GlobalScope.launch(Dispatchers.IO) {
            currentWeatherDao.upsert(fetchedWeather.currentWeatherEntry)
        }
    }
}