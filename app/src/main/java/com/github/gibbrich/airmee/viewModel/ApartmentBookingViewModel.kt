package com.github.gibbrich.airmee.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.gibbrich.airmee.core.manager.ResourceManager
import com.github.gibbrich.airmee.core.model.BookingRange
import com.github.gibbrich.airmee.core.repository.ApartmentsRepository
import com.github.gibbrich.airmee.di.DI
import javax.inject.Inject

class ApartmentViewModel(private val apartmentId: Int) : ViewModel() {

    @Inject
    internal lateinit var resourceManager: ResourceManager

    @Inject
    internal lateinit var apartmentsRepository: ApartmentsRepository

    init {
        DI.appComponent.inject(this)
    }

    private val bookingRangeSource = MutableLiveData<BookingRange>(null)
    val bookingRange: LiveData<BookingRange> = bookingRangeSource

    val apartment by lazy {
        apartmentsRepository
            .cachedApartments
            .value
            ?.find { it.id == apartmentId }!!
    }

    fun onConfirmDatesButtonClick(range: BookingRange) {
        bookingRangeSource.value = range
    }

    fun onBookButtonClick() {
        apartmentsRepository.bookApartment(apartmentId, bookingRangeSource.value!!)
    }
}

class ApartmentViewModelFactory(private val apartmentId: Int) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        ApartmentViewModel(apartmentId) as T
}