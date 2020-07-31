package io.github.occultus73.weatherforecast

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
import io.github.occultus73.weatherforecast.model.db.WeatherDatabase
import io.github.occultus73.weatherforecast.model.network.*
import io.github.occultus73.weatherforecast.model.provider.UnitProvider
import io.github.occultus73.weatherforecast.model.provider.UnitProviderImpl
import io.github.occultus73.weatherforecast.model.repository.WeatherRepository
import io.github.occultus73.weatherforecast.model.repository.WeatherRepositoryImpl
import io.github.occultus73.weatherforecast.view.weather.current.CurrentWeatherViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class WeatherApplication() : Application(), KodeinAware {
    override val kodein: Kodein = Kodein.lazy {
        import(androidXModule(this@WeatherApplication))

        // Room Database Instances
        bind() from singleton { WeatherDatabase(instance()) }
        bind() from singleton { instance<WeatherDatabase>().currentWeatherDao() }

        // Retrofit API Instances
        bind<ConnectivityInterceptor>() with singleton { ConnectivityInterceptorImpl(instance()) }
        bind() from singleton { OpenWeatherMapApiService(instance()) }
        bind() from singleton { ApixuWeatherApiService(instance())}
        bind<WeatherNetworkDataSource>() with singleton { WeatherNetworkDataSourceImpl(instance(), instance()) }

        // Repository that manages Room Database & Retrofit API
        bind<WeatherRepository>() with singleton { WeatherRepositoryImpl(instance(), instance())}

        // ViewModel for current weather fragment
        bind<UnitProvider>() with singleton { UnitProviderImpl(instance()) }
        bind() from provider { CurrentWeatherViewModelFactory(instance(), instance()) }
    }

    override fun onCreate() {
        super.onCreate()

        AndroidThreeTen.init(this)
    }
}