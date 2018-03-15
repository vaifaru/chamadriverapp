package br.com.chamaapp.driver.features.splash

interface SplashContract {
  interface View {
    fun showInfoPermission()
    fun goToHome()
  }

  interface Presenter {
    fun askLocationPermission()
  }
}