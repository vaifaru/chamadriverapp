package br.com.chamaapp.driver.features.splash

import android.os.Bundle
import android.support.v7.app.AlertDialog
import br.com.chamaapp.driver.R
import br.com.chamaapp.driver.features.home.HomeActivity
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject


class SplashActivity : DaggerAppCompatActivity(), SplashContract.View {

  @Inject
  lateinit var presenter: SplashPresenter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_splash)

    presenter.askLocationPermission()
  }

  override fun goToHome() {
    startActivity(HomeActivity.getCallingIntent(this))
    finish()
  }

  override fun showInfoPermission() {
    AlertDialog.Builder(this).create().apply {
      setTitle(getString(R.string.permission_needed))
      setMessage(getString(R.string.permission_needed_message))
      setButton(AlertDialog.BUTTON_NEUTRAL, "OK", { dialog, _ ->
        dialog.dismiss()
        presenter.askLocationPermission()
      })
      show()
    }
  }

}
