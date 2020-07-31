package io.github.occultus73.weatherforecast.model.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.github.occultus73.weatherforecast.model.db.entity.CurrentWeatherEntry
import io.github.occultus73.weatherforecast.util.KeyConstants.CURRENT_WEATHER_ID

@Dao
interface CurrentWeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(weatherEntry: CurrentWeatherEntry)

    @Query("select * from current_weather where id = $CURRENT_WEATHER_ID")
    fun getWeather(): LiveData<CurrentWeatherEntry>
}