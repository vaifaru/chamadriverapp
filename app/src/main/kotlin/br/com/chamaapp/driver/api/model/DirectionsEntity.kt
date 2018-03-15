package br.com.chamaapp.driver.api.model

import com.google.gson.annotations.SerializedName

class DirectionsApiEntity(
    val status: Status,
    val routes: List<Route>?
) {

  class Route(
      @SerializedName("overview_polyline")
      val polyline: Polyline?,
      val legs: Array<Leg>?
  )

  class Polyline(
      val points: String?
  )

  class Leg(
      val distance: TextValue,
      val duration: TextValue,
      @SerializedName("duration_in_traffic")
      val durationInTraffic: TextValue?
  )

  class TextValue(
      val text: String,
      val value: Double
  )

  enum class Status {
    OK, NOT_FOUND, ZERO_RESULTS, MAX_WAYPOINTS_EXCEEDED,
    INVALID_REQUEST, OVER_QUERY_LIMIT, REQUEST_DENIED, UNKNOWN_ERROR
  }

}