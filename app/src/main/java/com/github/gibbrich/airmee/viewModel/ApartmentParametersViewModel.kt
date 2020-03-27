package com.github.gibbrich.airmee.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.github.gibbrich.airmee.R
import com.github.gibbrich.airmee.core.model.Range
import com.github.gibbrich.airmee.core.repository.ResourceManager
import com.github.gibbrich.airmee.di.DI
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class ApartmentParametersViewModel : ViewModel() {

    @Inject
    internal lateinit var resourceManager: ResourceManager

    private val datesSource = MutableLiveData<Range>(null)
    val dates: LiveData<String> = datesSource.map(this::getRangeRepresentation)

    private val bedsSource = MutableLiveData(0)
    val beds: LiveData<Int> = bedsSource

    init {
        DI.appComponent.inject(this)
    }

    fun onChangeBedButtonClick(isIncrease: Boolean) {
        val currentBedsCount = bedsSource.value!!
        val result = if (isIncrease) {
            currentBedsCount.inc()
        } else {
            currentBedsCount.dec()
        }
        bedsSource.value = result.coerceAtLeast(0)
    }

    fun onConfirmDatesButtonClick(range: Range) {
        datesSource.value = range
    }

    private fun getRangeRepresentation(range: Range?): String = if (range == null) {
        resourceManager.getString(R.string.apartment_parameters_dates)
    } else {
        val formatter = SimpleDateFormat("dd MMM", Locale.getDefault())
        "${formatter.format(Date(range.start))} - ${formatter.format(Date(range.end))}"
    }
}

