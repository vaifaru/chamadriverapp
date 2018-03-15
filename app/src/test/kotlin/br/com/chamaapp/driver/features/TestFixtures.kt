package br.com.chamaapp.driver.features

import br.com.chamaapp.driver.api.model.LatLng
import br.com.chamaapp.driver.api.model.OrderResponse
import com.google.gson.annotations.SerializedName

fun createFakeOrderResponse(): OrderResponse {
  return OrderResponse(
      "123",
      LatLng(123.0, 123.0),
      LatLng(123.0, 123.0),
      "some state")
}