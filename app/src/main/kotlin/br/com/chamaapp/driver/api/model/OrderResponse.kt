package br.com.chamaapp.driver.api.model

import com.google.gson.annotations.SerializedName

data class OrderResponse(
    val id: String,
    val destination: LatLng,
    @SerializedName("current_location")
    val currentLocation: LatLng,
    val state: String) {
    companion object {
      val DELIVERED = "delivered"
    }
}
