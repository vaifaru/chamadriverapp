package br.com.chamaapp.driver.api

import br.com.chamaapp.driver.BuildConfig
import br.com.chamaapp.driver.api.model.LatLng
import br.com.chamaapp.driver.api.model.OrderResponse
import io.reactivex.Single
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface DriverApi {

  @GET("/order/{id}")
  fun getOrder(@Path("id") id: String) : Single<OrderResponse>

  @PUT("/order/{id}/location")
  fun updateOrder(@Path("id") id: String, @Body updateOrderRequest: LatLng): Single<OrderResponse>

  companion object {
    private const val URL = "https://murmuring-shelf-44841.herokuapp.com/"

    fun create(): DriverApi {
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

      return retrofit.create(DriverApi::class.java)
    }
  }
}
