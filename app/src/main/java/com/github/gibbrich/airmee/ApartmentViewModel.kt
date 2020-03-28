package com.github.gibbrich.airmee

import androidx.lifecycle.*
import com.github.gibbrich.airmee.core.model.BookingRange
import com.github.gibbrich.airmee.core.manager.ResourceManager
import com.github.gibbrich.airmee.core.model.Apartment
import com.github.gibbrich.airmee.core.repository.ApartmentsRepository
import com.github.gibbrich.airmee.di.DI
import java.text.SimpleDateFormat
import java.util.*
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
    val bookingRange: LiveData<String> = bookingRangeSource.map(::getRangeRepresentation)

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

    private fun getRangeRepresentation(bookingRange: BookingRange?): String =
        if (bookingRange == null) {
            resourceManager.getString(R.string.apartment_booking_select_dates)
        } else {
            val formatter = SimpleDateFormat("dd MMM", Locale.getDefault())
            "${formatter.format(Date(bookingRange.start))} - ${formatter.format(Date(bookingRange.end))}"
        }
}

class ApartmentViewModelFactory(private val apartmentId: Int) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        ApartmentViewModel(apartmentId) as T
}