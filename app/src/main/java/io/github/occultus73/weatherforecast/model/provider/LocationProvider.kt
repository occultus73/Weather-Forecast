package io.github.occultus73.weatherforecast.model.provider

import com.google.android.gms.maps.model.LatLng
import io.github.occultus73.weatherforecast.model.network.response.Location

interface LocationProvider {

    suspend fun hasLocationChanged(lastWeatherLocation: Location): Boolean
    suspend fun getPreferredLocation(): LatLng
}