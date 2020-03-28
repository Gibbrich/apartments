package com.github.gibbrich.airmee


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.github.gibbrich.airmee.core.model.BookingRange
import com.github.gibbrich.airmee.di.DI
import com.github.gibbrich.airmee.manager.INavigationManager
import com.github.gibbrich.airmee.utils.showDateRangePicker
import com.github.gibbrich.airmee.viewModel.ApartmentParametersViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import kotlinx.android.synthetic.main.fragment_apartment_parameters.*
import javax.inject.Inject

class ApartmentParametersFragment : Fragment() {

    @Inject
    lateinit var navigationManager: INavigationManager

    private val viewModel: ApartmentParametersViewModel by viewModels()

    init {
        DI.appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_apartment_parameters, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.dates.observe(this, Observer(::handleDates))
        viewModel.beds.observe(this, Observer(::handleBedsCount))

        apartment_parameters_pick_dates_button.setOnClickListener {
            showDateRangePicker(viewModel::onConfirmDatesButtonClick)
        }

        apartment_parameters_decrease_beds_count.setOnClickListener {
            viewModel.onChangeBedButtonClick(false)
        }

        apartment_parameters_increase_beds_count.setOnClickListener {
            viewModel.onChangeBedButtonClick(true)
        }

        apartment_parameters_show_all_variants.setOnClickListener {
            navigationManager.exit()
        }

        apartment_parameters_clear_filters.setOnClickListener {
            viewModel.onClearFiltersButtonClick()
        }
    }

    private fun handleDates(dates: String) {
        apartment_parameters_pick_dates_button.text = dates
    }

    private fun handleBedsCount(beds: Int) {
        apartment_parameters_beds_number_label.text = beds.toString()
    }
}
