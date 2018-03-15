package br.com.chamaapp.driver.services

import android.annotation.SuppressLint
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.model.LatLng
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

@SuppressLint("MissingPermission")
class LocationServiceImpl @Inject constructor(
    private val fusedLocationClient: FusedLocationProviderClient,
    private val locationRequest: LocationRequest
) : LocationService {

  private val publisher = PublishSubject.create<LatLng>()

  private val locationCallback = object : LocationCallback() {
    override fun onLocationResult(locationResult: LocationResult?) {
      locationResult ?: return
      for (location in locationResult.locations) {
        publisher.onNext(LatLng(location.latitude, location.longitude))
      }
    }
  }

  init {
    fusedLocationClient.lastLocation.addOnCompleteListener {
      val location = LatLng(it.result.latitude, it.result.longitude)
      publisher.onNext(location)
    }
  }

  override fun locationChanges(): Observable<LatLng> = publisher

  override fun startLocationUpdates() {
    fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
  }

  override fun stopLocationUpdates() {
    fusedLocationClient.removeLocationUpdates(locationCallback)
  }
}