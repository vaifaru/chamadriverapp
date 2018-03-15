package br.com.chamaapp.driver.features.home

import com.google.android.gms.maps.model.LatLng
import io.reactivex.Observable

object HomeContract {

  interface View {
    fun initMap()
    fun actionClicks() : Observable<Boolean>
    fun setStatus(started: Boolean)
    fun showMessage(started: Int)
    fun updateCurrentLocation(location: LatLng)
    fun addDestinationMarker(location: LatLng)
    fun playArrivedSong()
  }

  interface Presenter {
    fun onCreate()
  }

}