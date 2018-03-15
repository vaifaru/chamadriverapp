package br.com.chamaapp.driver.infra.di.module

import dagger.Module
import dagger.Provides
import dagger.Reusable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Module
class SchedulersModule {

  @Provides
  @Reusable
  fun provideSchedulersComposer() = SchedulersComposer()

}

@Singleton
open class SchedulersComposer @Inject constructor() {
  open fun executorScheduler(): Scheduler = Schedulers.io()
  open fun mainThreadScheduler(): Scheduler = AndroidSchedulers.mainThread()
}