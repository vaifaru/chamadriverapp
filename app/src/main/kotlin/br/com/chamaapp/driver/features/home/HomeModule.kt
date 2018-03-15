package br.com.chamaapp.driver.features.home

import android.content.Context
import br.com.chamaapp.driver.api.DriverApi
import br.com.chamaapp.driver.services.LocationService
import br.com.chamaapp.driver.services.LocationServiceImpl
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides

@Module
class HomeModule {

  @Provides
  fun providesView(homeActivity: HomeActivity): HomeContract.View = homeActivity

  @Provides
  fun providesLocationRequest(): LocationRequest {
    return LocationRequest().apply {
      interval = 5000

      fastestInterval = 1000
      priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }
  }

  @Provides
  fun providesFusedLocationClient(context: Context): FusedLocationProviderClient =
      LocationServices.getFusedLocationProviderClient(context)

  @Provides
  fun providesDriverApi(): DriverApi = DriverApi.create()

  @Provides
  fun providesLocationService(locationServiceImpl: LocationServiceImpl) : LocationService {
    return locationServiceImpl
  }

}