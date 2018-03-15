package br.com.chamaapp.driver.api

import br.com.chamaapp.driver.BuildConfig
import br.com.chamaapp.driver.api.model.DirectionsApiEntity
import io.reactivex.Single
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface GoogleMapsApi {

  @GET("maps/api/directions/json")
  fun getRoute(
      @Query("origin") origin: String,
      @Query("destination") destination: String,
      @Query("sensor") sensor: Boolean = true,
      @Query("departure_time") departureTime: String = "now"
  ): Single<DirectionsApiEntity>

  companion object {
    private const val URL = "https://maps.googleapis.com/"

    fun create(): GoogleMapsApi {
      val httpLoggingInterceptor = HttpLoggingInterceptor()

      httpLoggingInterceptor.level = when {
        BuildConfig.DEBUG -> HttpLoggingInterceptor.Level.BODY
        else -> HttpLoggingInterceptor.Level.NONE
      }

      val client = OkHttpClient.Builder()
          .addInterceptor(httpLoggingInterceptor)
          .build()

      val retrofit = Retrofit.Builder()
          .baseUrl(URL)
          .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
          .addConverterFactory(GsonConverterFactory.create())
          .client(client)
          .build()

      return retrofit.create(GoogleMapsApi::class.java)
    }
  }
}