package com.github.gibbrich.airmee

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.github.gibbrich.airmee.core.model.BookingRange
import com.github.gibbrich.airmee.core.manager.ResourceManager
import com.github.gibbrich.airmee.di.DI
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class ApartmentViewModel : ViewModel() {

    @Inject
    internal lateinit var resourceManager: ResourceManager

    init {
        DI.appComponent.inject(this)
    }

    private val bookingRangeSource = MutableLiveData<BookingRange>(null)
    val bookingRange: LiveData<String> = bookingRangeSource.map(::getRangeRepresentation)

    fun onConfirmDatesButtonClick(range: BookingRange) {
        bookingRangeSource.value = range
    }

    private fun getRangeRepresentation(bookingRange: BookingRange?): String = if (bookingRange == null) {
        resourceManager.getString(R.string.apartment_booking_select_dates)
    } else {
        val formatter = SimpleDateFormat("dd MMM", Locale.getDefault())
        "${formatter.format(Date(bookingRange.start))} - ${formatter.format(Date(bookingRange.end))}"
    }
}
