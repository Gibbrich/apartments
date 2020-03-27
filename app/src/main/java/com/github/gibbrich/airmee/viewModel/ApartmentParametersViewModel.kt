package com.github.gibbrich.airmee.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.github.gibbrich.airmee.R
import com.github.gibbrich.airmee.core.model.ApartmentFilter
import com.github.gibbrich.airmee.core.model.BookingRange
import com.github.gibbrich.airmee.core.repository.ApartmentParametersRepository
import com.github.gibbrich.airmee.core.repository.ResourceManager
import com.github.gibbrich.airmee.di.DI
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class ApartmentParametersViewModel : ViewModel() {

    @Inject
    internal lateinit var resourceManager: ResourceManager
    @Inject
    internal lateinit var apartmentParametersRepository: ApartmentParametersRepository

    init {
        DI.appComponent.inject(this)
    }

    val dates: LiveData<String> = apartmentParametersRepository.filter.map { getRangeRepresentation(it.bookingRange) }
    val beds: LiveData<Int> = apartmentParametersRepository.filter.map(ApartmentFilter::beds)

    fun onChangeBedButtonClick(isIncrease: Boolean) =
        apartmentParametersRepository.changeBedsQuantity(isIncrease)

    fun onConfirmDatesButtonClick(bookingRange: BookingRange) =
        apartmentParametersRepository.changeRange(bookingRange)

    fun onClearFiltersButtonClick() = apartmentParametersRepository.clearFilters()

    private fun getRangeRepresentation(bookingRange: BookingRange?): String = if (bookingRange == null) {
        resourceManager.getString(R.string.apartment_parameters_dates)
    } else {
        val formatter = SimpleDateFormat("dd MMM", Locale.getDefault())
        "${formatter.format(Date(bookingRange.start))} - ${formatter.format(Date(bookingRange.end))}"
    }
}

