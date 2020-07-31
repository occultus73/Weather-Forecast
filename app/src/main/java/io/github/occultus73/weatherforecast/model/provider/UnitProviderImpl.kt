package io.github.occultus73.weatherforecast.model.provider

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import io.github.occultus73.weatherforecast.internal.UnitSystem
import io.github.occultus73.weatherforecast.util.KeyConstants.UNIT_SYSTEM

class UnitProviderImpl(context: Context) : UnitProvider {
    private val appContext = context.applicationContext
    private val preferences: SharedPreferences
       get() = PreferenceManager.getDefaultSharedPreferences(appContext)

    override fun getUnitSystem(): UnitSystem {
        val selectedName = preferences.getString(UNIT_SYSTEM, UnitSystem.METRIC.name)
        return UnitSystem.valueOf(selectedName!!)
    }
}