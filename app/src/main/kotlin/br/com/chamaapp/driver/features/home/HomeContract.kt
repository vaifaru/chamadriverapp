package br.com.chamaapp.driver.features.home

import com.google.android.gms.maps.model.LatLng
import io.reactivex.Observable

object HomeContract {

  interface View {
    fun initMap()
    fun actionClicks() : Observable<Boolean>
    fun setStarted(started: Boolean)
    fun showMessage(started: Int)
    fun addDestinationMarker(location: LatLng)
    fun playArrivedSong()
    fun showRoute(points: String?)
  }

  interface Presenter {
    fun onCreate()
    fun onDestroy()
  }

}