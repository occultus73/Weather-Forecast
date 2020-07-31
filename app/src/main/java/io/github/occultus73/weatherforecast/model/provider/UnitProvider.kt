package io.github.occultus73.weatherforecast.model.provider

import io.github.occultus73.weatherforecast.internal.UnitSystem

interface UnitProvider {
    fun getUnitSystem(): UnitSystem
}