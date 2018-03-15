package br.com.chamaapp.driver.features.splash

import br.com.chamaapp.driver.infra.di.module.SchedulersComposer
import com.tbruyelle.rxpermissions2.RxPermissions
import dagger.Module
import dagger.Provides

@Module
class SplashModule {

  @Provides
  fun providesSplashPresenter(activity: SplashActivity, schedulersComposer: SchedulersComposer): SplashPresenter {
    return SplashPresenter(activity, RxPermissions(activity), schedulersComposer)
  }

}