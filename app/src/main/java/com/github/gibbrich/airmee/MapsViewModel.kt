package com.github.gibbrich.airmee

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.gibbrich.airmee.core.model.Apartment
import com.github.gibbrich.airmee.core.repository.ApartmentsRepository
import com.github.gibbrich.airmee.core.repository.LocationRepository
import com.github.gibbrich.airmee.di.DI
import com.github.gibbrich.airmee.model.ApartmentViewData
import kotlinx.coroutines.launch
import javax.inject.Inject

class MapsViewModel : ViewModel() {
    @Inject
    internal lateinit var apartmentsRepository: ApartmentsRepository
    @Inject
    internal lateinit var locationRepository: LocationRepository

    private val apartmentsSource = MutableLiveData<List<ApartmentViewData>>(emptyList())
    val apartments: LiveData<List<ApartmentViewData>> = apartmentsSource

    private val stateSource = MutableLiveData<LoadingState?>()
    val loadingState: LiveData<LoadingState?> = stateSource

    init {
        DI.appComponent.inject(this)
//        observeUserLocation()
    }

    fun getApartments() {
        if (stateSource.value == LoadingState.LOADING) {
            return
        }

        stateSource.value = LoadingState.LOADING
        viewModelScope.launch {
            val result = try {
                val apartments = apartmentsRepository.fetchApartments()
                stateSource.value = null
                apartments
            } catch (e: Exception) {
                e.printStackTrace()
                stateSource.value = LoadingState.ERROR
                emptyList<Apartment>()
            }

            apartmentsSource.value = getApartmentListItems(result, getUserLocation())
        }
    }

    fun getUserLocation() = locationRepository.getLocation()

    fun getLocationsSource() = locationRepository.locationSource

    fun startFetchingLocation() = locationRepository.connect()

    fun stopFetchingLocation() = locationRepository.disconnect()

    fun onChangeFiltersClick() {

    }

//    private fun observeUserLocation() = getLocationsSource().observeForever {
//        apartmentsSource.value = getApartmentListItems(
//            apartmentsRepository.cachedApartments,
//            it
//        )
//    }

    private fun getApartmentListItems(
        apartments: List<Apartment>,
        userLocation: Location
    ) = apartments
        .map { it.toApartmentListItem(userLocation) }
        .sortedBy(ApartmentViewData::distanceToUserKm)
}

enum class LoadingState {
    LOADING, ERROR
}

private fun Apartment.toApartmentListItem(userLocation: Location): ApartmentViewData {
    val distanceInMeters = Location("")
    distanceInMeters.latitude = latitude
    distanceInMeters.longitude = longitude
    val distanceToUserKm = distanceInMeters.distanceTo(userLocation) / 1000

    return ApartmentViewData(bedrooms, name, distanceToUserKm, latitude, longitude)
}