package io.github.occultus73.weatherforecast.model.db.entity.typeconverters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.github.occultus73.weatherforecast.model.db.entity.Weather
import io.github.occultus73.weatherforecast.model.network.response.CurrentWeatherResponse
import java.lang.reflect.Type

class WeatherListTypeConverter {
    @TypeConverter
    fun stringToMeasurements(json: String): List<Weather> {
        val type: Type = object: TypeToken<List<Weather>>(){}.type
        return Gson().fromJson(json, type)
    }

    @TypeConverter
    fun measurementsToString(list: List<Weather>): String {
        val type: Type = object: TypeToken<List<Weather>>(){}.type
        return Gson().toJson(list, type)
    }

    @TypeConverter
    fun stringToApixuResponse(json: String): CurrentWeatherResponse {
        val type: Type = object: TypeToken<CurrentWeatherResponse>(){}.type
        return Gson().fromJson(json, type)
    }

    @TypeConverter
    fun ApixuResponseToString(response: CurrentWeatherResponse): String {
        val type: Type = object: TypeToken<CurrentWeatherResponse>(){}.type
        return Gson().toJson(response, type)
    }
}
