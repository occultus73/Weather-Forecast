package io.github.occultus73.weatherforecast.model.network.response


import com.google.gson.annotations.SerializedName

data class Request(
    val language: String,
    val query: String,
    val type: String,
    val unit: String
)