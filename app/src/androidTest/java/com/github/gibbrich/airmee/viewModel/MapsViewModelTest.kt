package com.github.gibbrich.airmee.viewModel

import android.location.Location
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.test.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import com.github.gibbrich.airmee.AppTest
import com.github.gibbrich.airmee.core.model.Apartment
import com.github.gibbrich.airmee.core.model.ApartmentFilter
import com.github.gibbrich.airmee.core.repository.ApartmentFiltersRepository
import com.github.gibbrich.airmee.core.repository.ApartmentsRepository
import com.github.gibbrich.airmee.core.repository.LocationRepository
import com.github.gibbrich.airmee.data.repository.LocationDataRepository
import com.github.gibbrich.airmee.di.AppComponentMock
import com.github.gibbrich.airmee.di.DI
import com.github.gibbrich.airmee.model.ApartmentViewData
import com.github.gibbrich.rickandmorti.utils.MainCoroutineScopeRule
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.MockitoAnnotations
import javax.inject.Inject

/**
 * We need to use instrumental tests, as [MapsViewModel] use [Location]
 */
@RunWith(AndroidJUnit4::class)
class MapsViewModelTest {
    @get:Rule
    val coroutineScope =  MainCoroutineScopeRule()
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    internal lateinit var apartmentsRepository: ApartmentsRepository
    @Inject
    internal lateinit var locationRepository: LocationRepository
    @Inject
    internal lateinit var apartmentFiltersRepository: ApartmentFiltersRepository

    private lateinit var viewModel: MapsViewModel

    private val app by lazy {
        InstrumentationRegistry
            .getInstrumentation()
            .targetContext
            .applicationContext as AppTest
    }

    @Before
    fun setUp() {
        val component = app.provideAppComponent() as AppComponentMock
        DI.init(component)
        component.inject(this)
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun success_fetch_apartments_populate_MapsViewModel_apartments() = runBlocking {
        val cachedApartments = MutableLiveData<List<Apartment>>()
        val filter = MutableLiveData(ApartmentFilter())
        val locationSource = MutableLiveData(LocationDataRepository.DEFAULT_LOCATION)
        val apartments = listOf(
            Apartment(1, 1, "Noemia apt", 59.329440, 18.066045),
            Apartment(2, 1, "Thalia apt", 59.332532, 18.057945)
        )

        val viewDataList = apartments
            .map { it.toApartmentViewData(LocationDataRepository.DEFAULT_LOCATION) }
            .sortedBy(ApartmentViewData::distanceToUserKm)

        whenever(apartmentsRepository.cachedApartments).then { cachedApartments }
        whenever(apartmentFiltersRepository.filter).then { filter }
        whenever(locationRepository.locationSource).then { locationSource }
        whenever(apartmentsRepository.fetchApartments()).then {
            cachedApartments.value = apartments
            Unit
        }

        val observer = Observer<List<ApartmentViewData>> {
            assertEquals(viewDataList, it)
        }

        viewModel = MapsViewModel()
        viewModel.apartments.observeForever(observer)

        viewModel.fetchApartmentsIfNeed()
    }
}