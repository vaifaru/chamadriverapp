package br.com.chamaapp.driver.features.splash

import android.Manifest
import br.com.chamaapp.driver.infra.di.module.SchedulersComposer
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.schedulers.Schedulers

class SplashPresenter(
    private val view: SplashContract.View,
    private val rxPermissions: RxPermissions,
    private val schedulersComposer: SchedulersComposer) : SplashContract.Presenter {

  override fun askLocationPermission() {
    rxPermissions
        .request(Manifest.permission.ACCESS_FINE_LOCATION)
        .subscribeOn(schedulersComposer.executorScheduler())
        .subscribe { granted ->
          if (granted) {
            view.goToHome()
          } else {
            view.showInfoPermission()
          }
        }
  }

}