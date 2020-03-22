package com.github.gibbrich.airmee

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.gibbrich.airmee.core.model.Apartment
import com.github.gibbrich.airmee.core.repository.ApartmentsRepository
import com.github.gibbrich.airmee.core.repository.LocationRepository
import com.github.gibbrich.airmee.di.DI
import kotlinx.coroutines.launch
import javax.inject.Inject

class MapsViewModel : ViewModel() {
    @Inject
    internal lateinit var apartmentsRepository: ApartmentsRepository
    @Inject
    internal lateinit var locationRepository: LocationRepository

    private val apartmentsSource = MutableLiveData<List<Apartment>>(emptyList())
    val apartments: LiveData<List<Apartment>> = apartmentsSource

    private val stateSource = MutableLiveData<LoadingState?>()
    val loadingState: LiveData<LoadingState?> = stateSource

    init {
        DI.appComponent.inject(this)
    }

    fun getApartments() {
        if (stateSource.value == LoadingState.LOADING) {
            return
        }

        stateSource.value = LoadingState.LOADING
        viewModelScope.launch {
            val result = try {
                val apartments = apartmentsRepository.getApartments()
                stateSource.value = null
                apartments
            } catch (e: Exception) {
                e.printStackTrace()
                stateSource.value = LoadingState.ERROR
                emptyList<Apartment>()
            }

            apartmentsSource.value = result
        }
    }

    fun getUserLocation() = locationRepository.getLocation()

    fun getLocationsSource() = locationRepository.locationSource

    fun startFetchingLocation() = locationRepository.connect()

    fun stopFetchingLocation() = locationRepository.disconnect()
}

enum class LoadingState {
    LOADING, ERROR
}
