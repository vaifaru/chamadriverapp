package br.com.chamaapp.driver.features.home

import br.com.chamaapp.driver.R
import br.com.chamaapp.driver.api.DriverApi
import br.com.chamaapp.driver.api.model.OrderResponse
import br.com.chamaapp.driver.extensions.toInternal
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

  private val orderId = "5cc97000-2712-11e8-ba85-c917109fb6e3"
  private val disposables = CompositeDisposable()

  override fun onCreate() {
    view.initMap()

    getOrder().subscribe().compose()

    observeAndDispatchLocations().subscribe().compose()

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
        .retry()
  }

  internal fun observeAndDispatchLocations(): Observable<OrderResponse> {
    return locationService.locationChanges()
        .observeOn(schedulersComposer.mainThreadScheduler())
        .doOnNext { view.updateCurrentLocation(it) }
        .observeOn(schedulersComposer.executorScheduler())
        .flatMapSingle { api.updateOrder(orderId, it.toInternal) }
        .doOnNext {
          if (it.state == OrderResponse.DELIVERED) {
            view.playArrivedSong()
          }
        }
        .doOnError { view.showMessage(R.string.unexpected_error) }
        .retry()
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