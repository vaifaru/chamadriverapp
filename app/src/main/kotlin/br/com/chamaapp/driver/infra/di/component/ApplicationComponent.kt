package br.com.chamaapp.driver.infra.di.component

import br.com.chamaapp.driver.infra.di.module.ActivityBuilderModule
import br.com.chamaapp.driver.global.ApplicationModule
import br.com.chamaapp.driver.global.DriverApplication
import br.com.chamaapp.driver.infra.di.module.SchedulersModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
      (AndroidSupportInjectionModule::class),
      (ActivityBuilderModule::class),
      (SchedulersModule::class),
      (ApplicationModule::class)
    ]
)
interface ApplicationComponent : AndroidInjector<DriverApplication> {

  @Component.Builder
  interface Builder {
    @BindsInstance
    fun application(application: DriverApplication): Builder

    fun build(): ApplicationComponent
  }
}
