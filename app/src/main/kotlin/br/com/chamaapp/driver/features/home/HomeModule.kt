package br.com.chamaapp.driver.features.home

import android.content.Context
import br.com.chamaapp.driver.api.DriverApi
import br.com.chamaapp.driver.infra.di.module.SchedulersComposer
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides

@Module
class HomeModule {

  @Provides
  fun providesLocationRequest(): LocationRequest {
    return LocationRequest().apply {
      interval = 5000

      fastestInterval = 1000
      priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }
  }

  @Provides
  fun providesFusedLocationClient(context: Context) = LocationServices.getFusedLocationProviderClient(context)

  @Provides
  fun providesHomePresenter(activity: HomeActivity,
      locationService: LocationService,
      schedulersComposer: SchedulersComposer): HomePresenter {
    return HomePresenter(activity, locationService,  DriverApi.create(), schedulersComposer)
  }

  @Provides
  fun providesLocationService(fusedLocationClient: FusedLocationProviderClient,
      locationRequest: LocationRequest) : LocationService {
    return LocationService(fusedLocationClient, locationRequest)
  }

}