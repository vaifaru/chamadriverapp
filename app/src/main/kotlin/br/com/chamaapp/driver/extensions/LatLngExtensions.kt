package br.com.chamaapp.driver.extensions

import com.google.android.gms.maps.model.LatLng

val LatLng.toInternal: br.com.chamaapp.driver.api.model.LatLng
  get() = br.com.chamaapp.driver.api.model.LatLng(this.latitude, this.longitude)