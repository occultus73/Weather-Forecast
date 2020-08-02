package io.github.occultus73.weatherforecast.model.db.entity


import androidx.room.*
import com.google.gson.annotations.SerializedName
import io.github.occultus73.weatherforecast.model.db.entity.typeconverters.WeatherListTypeConverter
import io.github.occultus73.weatherforecast.model.network.response.CurrentWeatherResponse
import io.github.occultus73.weatherforecast.util.KeyConstants.CURRENT_WEATHER_ID

@Entity(tableName = "current_weather")
@TypeConverters(WeatherListTypeConverter::class)
data class CurrentWeatherEntry(
    val clouds: Int,
    @SerializedName("dew_point")
    val dewPoint: Double,
    val dt: Int,
    @SerializedName("feels_like")
    val feelsLike: Double,
    val humidity: Int,
    val pressure: Int,
    val rain: Float = 0.0f,
    val sunrise: Int,
    val sunset: Int,
    val temp: Double,
    val uvi: Double,
    val visibility: Int,
    val weather: List<Weather>,
    @SerializedName("wind_deg")
    val windDeg: Int,
    @SerializedName("wind_speed")
    val windSpeed: Double
){
    // Obviously we only ever want one entry in the database for local current weather.
    @PrimaryKey(autoGenerate = false)
    var id: Int = CURRENT_WEATHER_ID

    // SHAMELESSLY TACKED ON
    var apixuWeatherResponse: CurrentWeatherResponse? = null
}