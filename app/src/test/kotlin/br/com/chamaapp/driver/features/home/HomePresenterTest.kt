package br.com.chamaapp.driver.features.home

import br.com.chamaapp.driver.api.DriverApi
import br.com.chamaapp.driver.infra.di.module.SchedulersComposer
import br.com.chamaapp.driver.services.LocationService
import org.junit.Test
import org.mockito.Mock

class HomePresenterTest {

  @Mock
  lateinit var view: HomeContract.View

  @Mock
  lateinit var locationService: LocationService

  @Mock
  lateinit var api: DriverApi

  @Mock
  lateinit var schedulersComposer: SchedulersComposer

  @Mock
  lateinit var presenter: HomePresenter

  @Test
  fun `actionClicks$production_sources_for_module_app`() {
    val testObserver = presenter.actionClicks().test()
  }

  @Test
  fun `observeAndDispatchLocations$production_sources_for_module_app`() {
  }

  @Test
  fun `getOrder$production_sources_for_module_app`() {
  }

}