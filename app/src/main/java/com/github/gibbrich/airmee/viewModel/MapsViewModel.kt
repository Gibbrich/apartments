package com.github.gibbrich.airmee.viewModel

import android.location.Location
import androidx.lifecycle.*
import com.github.gibbrich.airmee.core.utils.combineLatest
import com.github.gibbrich.airmee.core.model.Apartment
import com.github.gibbrich.airmee.core.model.ApartmentFilter
import com.github.gibbrich.airmee.core.model.isNotIntersect
import com.github.gibbrich.airmee.core.repository.ApartmentFiltersRepository
import com.github.gibbrich.airmee.core.repository.ApartmentsRepository
import com.github.gibbrich.airmee.core.repository.LocationRepository
import com.github.gibbrich.airmee.di.DI
import com.github.gibbrich.airmee.model.ApartmentViewData
import com.github.gibbrich.airmee.model.CameraProperties
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.launch
import javax.inject.Inject

class MapsViewModel : ViewModel() {

    @Inject
    internal lateinit var apartmentsRepository: ApartmentsRepository
    @Inject
    internal lateinit var locationRepository: LocationRepository
    @Inject
    internal lateinit var apartmentFiltersRepository: ApartmentFiltersRepository

    init {
        DI.appComponent.inject(this)
    }

    val apartments: LiveData<List<ApartmentViewData>> = apartmentsRepository.cachedApartments
        .combineLatest(apartmentFiltersRepository.filter)
        .map {
            it.first
                .filterApartmentsList(it.second)
                .mapToApartmentViewData(locationRepository.locationSource.value!!)
        }

    private val errorSource = MutableLiveData(false)
    val error: LiveData<Boolean> = errorSource

    private val cameraPropertiesSource = MutableLiveData(
        CameraProperties(locationRepository.locationSource.value!!.toLatLng())
    )
    val cameraProperties: LiveData<CameraProperties> = cameraPropertiesSource

    fun fetchApartmentsIfNeed() {
        val shouldFetchApartments = apartmentsRepository.cachedApartments.value?.isNotEmpty() ?: true
        if (shouldFetchApartments.not()) {
            return
        }

        viewModelScope.launch {
            try {
                apartmentsRepository.fetchApartments()
            } catch (e: Exception) {
                e.printStackTrace()
                errorSource.value = true
            }
        }
    }

    fun startFetchingLocation() = locationRepository.connect()

    fun stopFetchingLocation() = locationRepository.disconnect()

    fun onZoomChange(isZoomIn: Boolean) {
        val cameraProperties = cameraPropertiesSource.value!!
        val zoom = if (isZoomIn) {
            cameraProperties.zoom.inc()
        } else {
            cameraProperties.zoom.dec()
        }
        cameraPropertiesSource.value = getNewCameraProperties(newZoom = zoom, shouldMove = false)
    }

    fun onCurrentLocationButtonClick() {
        val location = locationRepository.locationSource.value!!
        val currentLocation = LatLng(location.latitude, location.longitude)
        cameraPropertiesSource.value = getNewCameraProperties(newLatLng = currentLocation)
    }

    fun onScrollEnd(cardPosition: Int) {
        val adapterItems = apartments.value!!
        if (adapterItems.isEmpty()) {
            return
        }
        val data = adapterItems[cardPosition]
        val latLng = LatLng(data.latitude, data.longitude)
        cameraPropertiesSource.value = getNewCameraProperties(newLatLng = latLng)
    }

    fun onApartmentLongClick(apartmentId: Int) {
        val data = apartments.value!!.find { it.id == apartmentId }!!
        val latLng = LatLng(data.latitude, data.longitude)
        cameraPropertiesSource.value = getNewCameraProperties(newLatLng = latLng)
    }

    fun onMapMarkerClick(apartmentId: Int): Int =
        apartments.value!!.indexOfFirst { it.id == apartmentId }

    fun onRetryButtonClick() {
        errorSource.value = false
        fetchApartmentsIfNeed()
    }

    /**
     * After navigation to another screens and returning back
     * we don't want camera change it's location and show error view
     */
    fun onDestroyView() {
        cameraPropertiesSource.value = getNewCameraProperties(shouldMove = false)
        errorSource.value = false
    }

    private fun List<Apartment>.filterApartmentsList(filter: ApartmentFilter) =
        filter {
            it.bedrooms >= filter.beds &&
                    it.bookingRange.isNotIntersect(filter.bookingRange)
        }

    private fun List<Apartment>.mapToApartmentViewData(userLocation: Location) =
        map { it.toApartmentViewData(userLocation) }
            .sortedBy(ApartmentViewData::distanceToUserKm)

    private fun getNewCameraProperties(
        newLatLng: LatLng? = null,
        newZoom: Float? = null,
        shouldMove: Boolean = true
    ): CameraProperties {
        val currentProperties = cameraPropertiesSource.value!!
        val latLng = newLatLng ?: currentProperties.latLng
        val zoom = newZoom ?: currentProperties.zoom
        return CameraProperties(latLng, zoom, shouldMove)
    }
}

fun Apartment.toApartmentViewData(userLocation: Location): ApartmentViewData {
    val distanceInMeters = Location("")
    distanceInMeters.latitude = latitude
    distanceInMeters.longitude = longitude
    val distanceToUserKm = distanceInMeters.distanceTo(userLocation) / 1000

    return ApartmentViewData(id, bedrooms, name, distanceToUserKm, latitude, longitude)
}

private fun Location.toLatLng() = LatLng(latitude, longitude)