package br.com.chamaapp.driver.features.home

import android.annotation.SuppressLint
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.model.LatLng
import io.reactivex.subjects.PublishSubject

@SuppressLint("MissingPermission")
class LocationService(
    private val fusedLocationClient: FusedLocationProviderClient,
    private val locationRequest: LocationRequest) {

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

  fun locationChanges() = publisher

  fun startLocationUpdates() {
    fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
  }

  fun stopLocationUpdates() {
    fusedLocationClient.removeLocationUpdates(locationCallback)
  }

}