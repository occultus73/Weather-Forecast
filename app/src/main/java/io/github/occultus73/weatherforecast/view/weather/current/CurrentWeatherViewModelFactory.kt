package io.github.occultus73.weatherforecast.view.weather.current

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.github.occultus73.weatherforecast.model.provider.UnitProvider
import io.github.occultus73.weatherforecast.model.repository.WeatherRepository

class CurrentWeatherViewModelFactory(
    private val weatherRepository: WeatherRepository,
    private val unitProvider: UnitProvider
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CurrentWeatherViewModel(weatherRepository, unitProvider) as T
    }
}