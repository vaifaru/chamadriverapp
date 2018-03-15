package br.com.chamaapp.driver.infra.di.module

import br.com.chamaapp.driver.features.home.HomeActivity
import br.com.chamaapp.driver.features.home.HomeModule
import br.com.chamaapp.driver.features.home.HomeScoped
import br.com.chamaapp.driver.features.splash.SplashActivity
import br.com.chamaapp.driver.features.splash.SplashModule
import br.com.chamaapp.driver.features.splash.SplashScoped
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilderModule {

  @SplashScoped
  @ContributesAndroidInjector(modules = [(SplashModule::class)])
  internal abstract fun splashActivity(): SplashActivity


  @HomeScoped
  @ContributesAndroidInjector(modules = [(HomeModule::class)])
  internal abstract fun orderDetailActivity(): HomeActivity
}
