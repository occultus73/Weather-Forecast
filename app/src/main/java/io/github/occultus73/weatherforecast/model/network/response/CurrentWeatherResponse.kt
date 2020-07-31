package io.github.occultus73.weatherforecast.model.network.response

data class CurrentWeatherResponse(
    val current: Current,
    val location: Location,
    val request: Request
)