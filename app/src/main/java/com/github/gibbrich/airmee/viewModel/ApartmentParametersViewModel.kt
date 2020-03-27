package com.github.gibbrich.airmee.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.github.gibbrich.airmee.R
import com.github.gibbrich.airmee.core.model.Range
import com.github.gibbrich.airmee.core.repository.ApartmentParametersRepository
import com.github.gibbrich.airmee.core.repository.ApartmentsRepository
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

    val dates: LiveData<String> = apartmentParametersRepository.range.map(this::getRangeRepresentation)
    val beds: LiveData<Int> = apartmentParametersRepository.bedsNumber

    fun onChangeBedButtonClick(isIncrease: Boolean) {
        val currentBedsCount = apartmentParametersRepository.bedsNumber.value!!
        val result = if (isIncrease) {
            currentBedsCount.inc()
        } else {
            currentBedsCount.dec()
        }
        apartmentParametersRepository.bedsNumber.value = result.coerceAtLeast(0)
    }

    fun onConfirmDatesButtonClick(range: Range) {
        apartmentParametersRepository.range.value = range
    }

    private fun getRangeRepresentation(range: Range?): String = if (range == null) {
        resourceManager.getString(R.string.apartment_parameters_dates)
    } else {
        val formatter = SimpleDateFormat("dd MMM", Locale.getDefault())
        "${formatter.format(Date(range.start))} - ${formatter.format(Date(range.end))}"
    }
}

