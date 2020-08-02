package io.github.occultus73.weatherforecast.model.provider

import com.google.android.gms.maps.model.LatLng
import io.github.occultus73.weatherforecast.model.network.response.Location

class LocationProviderImpl : LocationProvider {
    override suspend fun hasLocationChanged(lastWeatherLocation: Location): Boolean {
        return true
    }

    override suspend fun getPreferredLocation(): LatLng {
        return LatLng(51.283791, 0.0831033)
    }
}