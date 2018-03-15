package br.com.chamaapp.driver.global

import android.content.Context
import dagger.Module
import dagger.Provides

@Module
class ApplicationModule {

  @Provides
  fun provideContext(application: DriverApplication): Context = application

}
