package br.com.chamaapp.driver.infra.di.model

import br.com.chamaapp.driver.api.model.DirectionsApiEntity
import timber.log.Timber

class DirectionsMapper {
  companion object {
    fun parseDirection(direction: DirectionsApiEntity): Route? {
      if (direction.status == DirectionsApiEntity.Status.OK) {

        val firstRoute = direction.routes?.firstOrNull()
        firstRoute?.let { route ->

          Timber.i("Parsing route $route")

          route.polyline?.points?.let { points ->
            var distance = 0.0
            var duration = 0.0
            var durationWithTraffic = 0.0

            route.legs?.forEach {
              distance += it.distance.value
              duration += it.duration.value
              durationWithTraffic += it.durationInTraffic?.value ?: it.duration.value
            }

            return Route(
                bounds = null,
                distance = distance.toInt(),
                points = points,
                duration = duration.toInt(),
                durationWithTraffic = durationWithTraffic.toInt()
            )
          } ?: Timber.i("Failing on parse the route: $route")
        }
      }

      return null
    }
  }
}