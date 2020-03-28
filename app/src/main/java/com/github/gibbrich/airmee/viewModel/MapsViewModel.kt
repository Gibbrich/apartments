package com.github.gibbrich.airmee.viewModel

import android.location.Location
import androidx.lifecycle.*
import com.github.gibbrich.airmee.core.combineLatest
import com.github.gibbrich.airmee.core.model.Apartment
import com.github.gibbrich.airmee.core.model.ApartmentFilter
import com.github.gibbrich.airmee.core.model.isNotIntersect
import com.github.gibbrich.airmee.core.repository.ApartmentParametersRepository
import com.github.gibbrich.airmee.core.repository.ApartmentsRepository
import com.github.gibbrich.airmee.core.repository.LocationRepository
import com.github.gibbrich.airmee.di.DI
import com.github.gibbrich.airmee.model.ApartmentViewData
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.launch
import javax.inject.Inject

class MapsViewModel : ViewModel() {
    companion object {
        private const val DEFAULT_ZOOM = 15f
    }

    @Inject
    internal lateinit var apartmentsRepository: ApartmentsRepository
    @Inject
    internal lateinit var locationRepository: LocationRepository
    @Inject
    internal lateinit var apartmentParametersRepository: ApartmentParametersRepository

    init {
        DI.appComponent.inject(this)
    }

    val apartments: LiveData<List<ApartmentViewData>> = apartmentsRepository.cachedApartments
        .combineLatest(apartmentParametersRepository.filter)
        .map {
            it.first
                .filterApartmentsList(it.second)
                .mapToApartmentViewData(getUserLocation())
        }

    private val stateSource = MutableLiveData<LoadingState?>()
    val loadingState: LiveData<LoadingState?> = stateSource

    private val cameraPositionSource = MutableLiveData<LatLng>()
    val cameraPosition: LiveData<LatLng> = cameraPositionSource

    private val cameraZoomSource = MutableLiveData(DEFAULT_ZOOM)
    val cameraZoom: LiveData<Float> = cameraZoomSource

    fun fetchApartments() {
        val shouldFetchApartments = apartmentsRepository.cachedApartments.value?.isNotEmpty() ?: true
        if (stateSource.value == LoadingState.LOADING || shouldFetchApartments) {
            return
        }

        stateSource.value = LoadingState.LOADING
        viewModelScope.launch {
            try {
                apartmentsRepository.fetchApartments()
                stateSource.value = null
            } catch (e: Exception) {
                e.printStackTrace()
                stateSource.value = LoadingState.ERROR
            }
        }
    }

    fun getUserLocation() = locationRepository.getLocation()

    fun getLocationsSource() = locationRepository.locationSource

    fun startFetchingLocation() = locationRepository.connect()

    fun stopFetchingLocation() = locationRepository.disconnect()

    fun onChangeFiltersClick() {

    }

    fun onZoomChange(isZoomIn: Boolean) {
        val currentZoom = cameraZoomSource.value!!
        cameraZoomSource.value = if (isZoomIn) {
            currentZoom.inc()
        } else {
            currentZoom.dec()
        }
    }

    fun onCurrentLocationButtonClick() {
        val location = locationRepository.locationSource.value!!
        cameraPositionSource.value = LatLng(location.latitude, location.longitude)
    }

    fun onScrollEnd(cardPosition: Int) {
        val data = apartments.value!![cardPosition]
        cameraPositionSource.value = LatLng(data.latitude, data.longitude)
    }

    private fun List<Apartment>.filterApartmentsList(filter: ApartmentFilter) =
        filter {
            it.bedrooms >= filter.beds &&
                    it.bookingRange.isNotIntersect(filter.bookingRange)
        }

    private fun List<Apartment>.mapToApartmentViewData(userLocation: Location) =
        map { it.toApartmentViewData(userLocation) }
            .sortedBy(ApartmentViewData::distanceToUserKm)
}

enum class LoadingState {
    LOADING, ERROR
}

private fun Apartment.toApartmentViewData(userLocation: Location): ApartmentViewData {
    val distanceInMeters = Location("")
    distanceInMeters.latitude = latitude
    distanceInMeters.longitude = longitude
    val distanceToUserKm = distanceInMeters.distanceTo(userLocation) / 1000

    return ApartmentViewData(id, bedrooms, name, distanceToUserKm, latitude, longitude)
}