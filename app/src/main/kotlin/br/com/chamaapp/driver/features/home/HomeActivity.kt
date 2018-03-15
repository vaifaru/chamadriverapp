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

  override fun actionClicks(): Observable<Boolean> = RxView.clicks(action).map { action.started }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_home)
    presenter.onCreate()
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
    }
  }

  override fun setStatus(started: Boolean) {
    action.setStatus(started)
  }

  override fun updateCurrentLocation(location: LatLng) {
    withMap {
      val latLngBuilder = LatLngBounds.builder().apply {
        include(location)
        destinationMarker?.let {
          include(it.position)
        }
      }

      animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBuilder.build(), 200))
    }
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
