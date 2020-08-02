package io.github.occultus73.weatherforecast.view.weather.current

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import io.github.occultus73.weatherforecast.R
import io.github.occultus73.weatherforecast.internal.UnitSystem
import io.github.occultus73.weatherforecast.internal.glide.GlideApp
import io.github.occultus73.weatherforecast.view.base.ScopedFragment
import kotlinx.android.synthetic.main.current_weather_fragment.*
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class CurrentWeatherFragment : ScopedFragment(), KodeinAware {
    override val kodein: Kodein by closestKodein()

    private val viewModelFactory: CurrentWeatherViewModelFactory by instance()

    private lateinit var viewModel: CurrentWeatherViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.current_weather_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel =
            ViewModelProvider(this, viewModelFactory).get(CurrentWeatherViewModel::class.java)

        bindUI()
    }

    private fun bindUI() = launch {
        val currentWeather = viewModel.currentWeather.await()
        currentWeather.observe(viewLifecycleOwner, Observer {
            if(it == null) return@Observer
            group_loading.visibility = View.GONE
            updateLocation(it.apixuWeatherResponse?.location?.name?: "")
            updateDateToToday()
            updateTemperatures(it.temp, it.feelsLike)
            updateCondition(it.weather[0].description)
            updatePrecipitation(it.apixuWeatherResponse?.current?.precip ?: it.rain)
            updateWind(it.windDeg, it.windSpeed)
            updateCloudiness(it.clouds)

            GlideApp.with(this@CurrentWeatherFragment)
                .load("${it.apixuWeatherResponse?.current?.weatherIcons?.get(0)}")
                .into(imageView_condition_icon)
        })
    }

    private fun chooseLocalizedUnitAbbreviation(
        metric: String,
        imperial: String,
        scientific: String
    ): String {
        return when (viewModel.unitSystem) {
            UnitSystem.METRIC -> metric
            UnitSystem.IMPERIAL -> imperial
            UnitSystem.SCIENTIFIC -> scientific
        }
    }

    private fun updateLocation(location: String) {
        (requireActivity() as AppCompatActivity).supportActionBar?.title = location
    }

    private fun updateDateToToday() {
        (requireActivity() as AppCompatActivity).supportActionBar?.subtitle = "Today"
    }

    private fun updateTemperatures(temperature: Double, feelsLike: Double) {
        val unitAbbreviation = chooseLocalizedUnitAbbreviation("°C", "°F", "K")

        textView_temperature.text = "$temperature$unitAbbreviation"
        textView_feels_like_temperature.text = "Feels like $feelsLike$unitAbbreviation"
    }

    private fun updateCondition(condition: String) {
        textView_condition.text = condition
    }

    private fun updatePrecipitation(precipitationVolume: Float) {
        val unitAbbreviation = chooseLocalizedUnitAbbreviation("mm", "mm", "mm")

        textView_precipitation.text = "Precipitation: $precipitationVolume $unitAbbreviation"
    }

    private fun updateWind(windDirection: Int, windSpeed: Double) {
        val unitAbbreviation = chooseLocalizedUnitAbbreviation("m/s", "m/h", "m/s")

        textView_wind.text = "Wind: $windDirection°, $windSpeed$unitAbbreviation"
    }

    private fun updateCloudiness(cloudiness: Int) {
        textView_cloudiness.text = "Cloudiness: $cloudiness%"
    }
}