package io.github.occultus73.weatherforecast.model.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import io.github.occultus73.weatherforecast.model.network.response.OneCallResponse
import kotlinx.coroutines.Deferred
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherMapApiService {

    // OpenWeatherMap OneCall request.
    // https://api.openweathermap.org/data/2.5/onecall?lat=51.283791&lon=-0.0831033&appid=1d984e37de16d449047be61caaf7e07d&units=metric
    @GET("data/2.5/onecall")
    fun getCurrentWeather(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("units") units: String
    ): Deferred<OneCallResponse>

    // Retrofit Instance
    companion object {
        private const val API_KEY = "1d984e37de16d449047be61caaf7e07d"

        operator fun invoke(
            connectivityInterceptor: ConnectivityInterceptor
        ): OpenWeatherMapApiService {
            // intercept every API request URL we make to tack on the API_KEY as an additional query
            val requestInterceptor = Interceptor {

                val url = it.request()
                    .url
                    .newBuilder()
                    .addQueryParameter("appid",
                        API_KEY
                    )
                    .build()

                val request = it.request()
                    .newBuilder()
                    .url(url)
                    .build()

                return@Interceptor it.proceed(request)
            }

            // add logger to okHttpClient so we can see what it is doing wrong.
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BASIC)

            // create a custom okHttpClient to put our interceptor in
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(requestInterceptor)
                .addInterceptor(connectivityInterceptor)
                .addInterceptor(logging)
                .build()

            // build retrofit with our custom okHttpClient
            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("https://api.openweathermap.org/")
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(OpenWeatherMapApiService::class.java)
        }
    }
}