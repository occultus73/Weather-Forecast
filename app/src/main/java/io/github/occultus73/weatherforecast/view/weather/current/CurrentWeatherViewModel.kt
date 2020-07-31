package io.github.occultus73.weatherforecast.view.weather.current

import androidx.lifecycle.ViewModel
import io.github.occultus73.weatherforecast.internal.UnitSystem
import io.github.occultus73.weatherforecast.internal.lazyDeferred
import io.github.occultus73.weatherforecast.model.provider.UnitProvider
import io.github.occultus73.weatherforecast.model.repository.WeatherRepository
import java.util.*

class CurrentWeatherViewModel(
    private val weatherRepository: WeatherRepository,
    unitProvider: UnitProvider
) : ViewModel() {
    val unitSystem = unitProvider.getUnitSystem()

    val currentWeather by lazyDeferred {
        //Convert Unit enum to string for use as API argument.
        weatherRepository.getCurrentWeather(unitSystem.name.toLowerCase(Locale.ROOT))
    }
}