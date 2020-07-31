package io.github.occultus73.weatherforecast.model.db.entity


import com.google.gson.annotations.SerializedName

data class Weather(
    val description: String,
    val icon: String,
    val id: Int,
    val main: String
)