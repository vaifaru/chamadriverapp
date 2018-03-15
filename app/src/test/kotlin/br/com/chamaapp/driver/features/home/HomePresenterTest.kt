package br.com.chamaapp.driver.features.home

import br.com.chamaapp.driver.api.DriverApi
import br.com.chamaapp.driver.api.GoogleMapsApi
import br.com.chamaapp.driver.features.createFakeDeliveredOrderResponse
import br.com.chamaapp.driver.features.createFakeOrderResponse
import br.com.chamaapp.driver.infra.di.module.SchedulersComposer
import br.com.chamaapp.driver.services.LocationService
import com.google.android.gms.maps.model.LatLng
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.inOrder
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.TestScheduler
import io.reactivex.subjects.PublishSubject
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.never
import org.mockito.junit.MockitoJUnitRunner
import java.lang.Exception
import kotlin.math.abs

@RunWith(MockitoJUnitRunner::class)
class HomePresenterTest {

  @Mock
  lateinit var view: HomeContract.View

  @Mock
  lateinit var locationService: LocationService

  @Mock
  lateinit var driverApi: DriverApi

  @Mock
  lateinit var googleMapsApi: GoogleMapsApi

  @Mock
  lateinit var schedulersComposer: SchedulersComposer

  private val testScheduler = TestScheduler()
  private var orderResponse = createFakeOrderResponse()

  private val actionClicks = PublishSubject.create<Boolean>()

  @InjectMocks
  lateinit var presenter: HomePresenter

  @Before
  fun setup() {
    whenever(schedulersComposer.executorScheduler()).thenReturn(testScheduler)
    whenever(schedulersComposer.mainThreadScheduler()).thenReturn(testScheduler)
    whenever(locationService.locationChanges()).thenReturn(Observable.empty())
    whenever(driverApi.getOrder(any())).thenReturn(Single.just(orderResponse))
    whenever(view.actionClicks()).thenReturn(actionClicks)
  }

  @Test
  fun getOrder_withConnection_shouldShowDestinationMarker() {
    whenever(driverApi.getOrder(any())).thenReturn(Single.just(orderResponse))

    presenter.onCreate()

    testScheduler.triggerActions()

    verify(view).addDestinationMarker(any())
  }

  @Test
  fun getOrder_withoutConnection_shouldDisplayErrorMessage() {
    whenever(driverApi.getOrder(any())).thenReturn(Single.error(Exception()))

    presenter.onCreate()

    testScheduler.triggerActions()

    verify(view, never()).addDestinationMarker(any())
    verify(view).showMessage(any())
  }

  @Test
  fun getLocations_shouldUpdateOrderPosition() {
    whenever(locationService.locationChanges()).thenReturn(Observable.just(LatLng(123.0, 123.9)))

    presenter.onCreate()

    testScheduler.triggerActions()

    verify(driverApi).updateOrder(any(), any())
  }

  @Test
  fun getLocations_withDelivered_shouldPlaySound() {
    whenever(driverApi.updateOrder(any(), any())).thenReturn(Single.just(createFakeDeliveredOrderResponse()))

    whenever(locationService.locationChanges()).thenReturn(Observable.just(LatLng(123.0, 123.9)))

    presenter.onCreate()

    testScheduler.triggerActions()

    verify(locationService).stopLocationUpdates()
    verify(view).playArrivedSong()
    verify(view).setStarted(false)
  }
}
