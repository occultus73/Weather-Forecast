package io.github.occultus73.weatherforecast.model.network.response


import com.google.gson.annotations.SerializedName
import io.github.occultus73.weatherforecast.model.db.entity.CurrentWeatherEntry
import io.github.occultus73.weatherforecast.model.db.entity.Daily
import io.github.occultus73.weatherforecast.model.db.entity.Hourly

data class OneCallResponse(
    @SerializedName("current")
    val currentWeatherEntry: CurrentWeatherEntry,
    val daily: List<Daily>,
    val hourly: List<Hourly>,
    val lat: Double,
    val lon: Double,
    val timezone: String,
    @SerializedName("timezone_offset")
    val timezoneOffset: Int
)