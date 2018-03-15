package br.com.chamaapp.driver.features.home

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import br.com.chamaapp.driver.R
import br.com.chamaapp.driver.extensions.playSound
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.android.PolyUtil
import com.jakewharton.rxbinding2.view.RxView
import dagger.android.support.DaggerAppCompatActivity
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_home.action
import javax.inject.Inject

class HomeActivity : DaggerAppCompatActivity(), HomeContract.View {

  @Inject
  lateinit var presenter: HomePresenter

  private val mapFragment by lazy { SupportMapFragment.newInstance() }

  private var destinationMarker: Marker? = null
  private var currentPolyline: Polyline? = null

  override fun actionClicks(): Observable<Boolean> = RxView.clicks(action).map { action.started }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_home)
    presenter.onCreate()
  }

  override fun onDestroy() {
    super.onDestroy()
    presenter.onDestroy()
  }

  override fun showMessage(resId: Int) {
    Toast.makeText(this@HomeActivity, resId, Toast.LENGTH_SHORT).show()
  }

  @SuppressLint("MissingPermission")
  override fun initMap() {
    val fragmentTransaction = supportFragmentManager.beginTransaction()
    fragmentTransaction.add(R.id.mapView, mapFragment, "MapFragment")
    fragmentTransaction.commit()

    withMap {
      isMyLocationEnabled = true
      setMaxZoomPreference(17f)
      setPadding(10, 10, 10, 10)
    }
  }

  override fun setStarted(started: Boolean) {
    action.setStatus(started)
  }

  override fun addDestinationMarker(location: LatLng) {
    withMap {
      if (destinationMarker == null) {
        val options = MarkerOptions()
        options.position(location)
        destinationMarker = addMarker(options)
      } else {
        destinationMarker?.position = location
      }
    }

  }

  override fun showRoute(points: String?) {
    withMap {
      currentPolyline?.let {
        it.remove()
      }

      val options = PolylineOptions()
          .color(R.color.colorAccent)
          .width(8f)
          .addAll(PolyUtil.decode(points))

      currentPolyline = addPolyline(options)
      fitZoom()
    }
  }

  private fun fitZoom() {
    withMap {
      val latLngBuilder = LatLngBounds.builder().apply {
        destinationMarker?.let {
          include(it.position)
        }
        currentPolyline?.points?.forEach {
          include(it)
        }
      }
      animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBuilder.build(), 200))
    }
  }

  private fun withMap(action: GoogleMap.() -> Unit) {
    mapFragment.getMapAsync { it.action() }
  }

  override fun playArrivedSong() {
    playSound(R.raw.arrived_sound)
  }

  companion object {
    fun getCallingIntent(context: Context) = Intent(context, HomeActivity::class.java)
  }

}
