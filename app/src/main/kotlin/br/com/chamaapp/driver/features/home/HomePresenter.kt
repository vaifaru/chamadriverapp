package br.com.chamaapp.driver.features.home

import br.com.chamaapp.driver.R
import br.com.chamaapp.driver.api.DriverApi
import br.com.chamaapp.driver.api.GoogleMapsApi
import br.com.chamaapp.driver.api.model.DirectionsApiEntity
import br.com.chamaapp.driver.api.model.OrderResponse
import br.com.chamaapp.driver.extensions.toInternal
import br.com.chamaapp.driver.infra.di.model.DirectionsMapper
import br.com.chamaapp.driver.infra.di.module.SchedulersComposer
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.maps.model.LatLng
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

class HomePresenter(
    private val view: HomeActivity,
    private val locationService: LocationService,
    private val api: DriverApi,
    private val schedulersComposer: SchedulersComposer
) : HomeContract.Presenter, LocationCallback() {

  private val orderId = "78e8c5a0-286c-11e8-b6db-579bdd5713bc"
  private val disposables = CompositeDisposable()

  override fun onCreate() {
    view.initMap()

    getOrder().subscribe().compose()

    observeAndDispatchLocations()

    actionClicks().subscribe().compose()
  }

  override fun onDestroy() {
    disposables.clear()
  }

  internal fun actionClicks(): Observable<Boolean> {
    return view.actionClicks()
        .observeOn(schedulersComposer.mainThreadScheduler())
        .doOnNext { started ->
          when {
            started -> {
              locationService.stopLocationUpdates()
              view.setStarted(false)
              view.showMessage(R.string.location_sending_stopped)
            }
            else -> {
              locationService.startLocationUpdates()
              view.setStarted(true)
              view.showMessage(R.string.location_sending_started)
            }
          }
        }
        .retry()
  }

  private val googleapi = GoogleMapsApi.create()

  internal fun observeAndDispatchLocations() {
    locationService.locationChanges()
        .observeOn(schedulersComposer.mainThreadScheduler())
        .doOnNext { view.updateCurrentLocation(it) }
        .observeOn(schedulersComposer.executorScheduler())
        .flatMapSingle { api.updateOrder(orderId, it.toInternal) }
        .doOnNext {
          if (it.state == OrderResponse.DELIVERED) {
            locationService.stopLocationUpdates()
            view.playArrivedSong()
            view.setStarted(false)
          }
        }
        .doOnError { view.showMessage(R.string.unexpected_error) }
        .flatMapSingle {
          googleapi.getRoute(
              "${it.currentLocation.lat}, ${it.currentLocation.lng}",
              "${it.destination.lat},${it.destination.lng}",
              true)
        }
        .observeOn(schedulersComposer.mainThreadScheduler())
        .doOnNext { parseRoute(it) }
        .retry()
        .subscribe()
        .compose()
  }

  private fun parseRoute(directionsApiEntity: DirectionsApiEntity) {
    val route = DirectionsMapper.parseDirection(directionsApiEntity)
    route?.let { view.showRoute(it.points) }
  }

  internal fun getOrder(): Single<OrderResponse> {
    return api.getOrder(orderId)
        .subscribeOn(schedulersComposer.executorScheduler())
        .observeOn(schedulersComposer.mainThreadScheduler())
        .doOnSuccess { view.addDestinationMarker(LatLng(it.destination.lat, it.destination.lng)) }
        .doOnError { view.showMessage(R.string.unexpected_error) }
        .retry()
  }

  private fun Disposable.compose() = disposables.add(this)

}