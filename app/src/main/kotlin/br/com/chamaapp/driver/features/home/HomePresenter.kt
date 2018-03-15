package br.com.chamaapp.driver.features.home

import android.annotation.SuppressLint
import android.util.Log
import br.com.chamaapp.driver.R
import br.com.chamaapp.driver.api.DriverApi
import br.com.chamaapp.driver.api.model.OrderResponse
import br.com.chamaapp.driver.extensions.toInternal
import br.com.chamaapp.driver.infra.di.module.SchedulersComposer
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.maps.model.LatLng

class HomePresenter(
    private val view: HomeActivity,
    private val locationService: LocationService,
    private val api: DriverApi,
    private val schedulersComposer: SchedulersComposer
    ) : HomeContract.Presenter, LocationCallback() {

  var orderId = "5cc97000-2712-11e8-ba85-c917109fb6e3"

  @SuppressLint("MissingPermission")
  override fun onCreate() {
    view.initMap()

    api.getOrder(orderId)
        .subscribeOn(schedulersComposer.executorScheduler())
        .observeOn(schedulersComposer.mainThreadScheduler())
        .doOnSuccess { view.addDestinationMarker(LatLng(it.destination.lat, it.destination.lng)) }
        .doOnError {
          view.showMessage(R.string.unexpected_error)
          Log.e("Chama", it.message)
        }
        .retry()
        .subscribe()

    locationService.locationChanges()
        .observeOn(schedulersComposer.mainThreadScheduler())
        .doOnNext { view.updateCurrentLocation(it) }
        .observeOn(schedulersComposer.executorScheduler())
        .flatMapSingle { api.updateOrder(orderId, it.toInternal) }
        .doOnNext {
          if (it.state == OrderResponse.DELIVERED) {
            view.playArrivedSong()
          }
        }
        .doOnError {
          view.showMessage(R.string.unexpected_error)
          Log.e("Chama", "Network Error", it)
        }
        .retry()
        .subscribe()

    view.actionClicks()
        .subscribe { started ->
          when {
            started -> {
              locationService.stopLocationUpdates()
              view.setStatus(false)
              view.showMessage(R.string.location_sending_stopped)
            }
            else -> {
              locationService.startLocationUpdates()
              view.setStatus(true)
              view.showMessage(R.string.location_sending_started)
            }
          }
        }

  }

}