package br.com.chamaapp.driver.services

import com.google.android.gms.maps.model.LatLng
import io.reactivex.Observable


interface LocationService {
  fun locationChanges(): Observable<LatLng>
  fun startLocationUpdates()
  fun stopLocationUpdates()
}
