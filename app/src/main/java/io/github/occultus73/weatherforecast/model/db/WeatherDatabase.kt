package io.github.occultus73.weatherforecast.model.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import io.github.occultus73.weatherforecast.model.db.entity.CurrentWeatherEntry

@Database(entities = [CurrentWeatherEntry::class], version = 2)
abstract class WeatherDatabase : RoomDatabase() {
    abstract fun currentWeatherDao(): CurrentWeatherDao

    //thread safe singleton initializer of this database
    companion object {
        @Volatile
        private var instance: WeatherDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                WeatherDatabase::class.java,
                "weather.db"
            ).fallbackToDestructiveMigration().build()
    }
}