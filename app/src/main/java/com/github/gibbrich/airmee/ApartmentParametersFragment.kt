package com.github.gibbrich.airmee


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.github.gibbrich.airmee.core.model.Range
import com.github.gibbrich.airmee.viewModel.ApartmentParametersViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import kotlinx.android.synthetic.main.fragment_apartment_parameters.*

class ApartmentParametersFragment : Fragment() {

    private val viewModel: ApartmentParametersViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_apartment_parameters, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.dates.observe(this, Observer(::handleDates))
        viewModel.beds.observe(this, Observer(::handleBedsCount))

        apartment_parameters_pick_dates_button.setOnClickListener {
            MaterialDatePicker.Builder
                .dateRangePicker()
                .build()
                .apply {
                    addOnPositiveButtonClickListener {
                        viewModel.onConfirmDatesButtonClick(Range(it.first!!, it.second!!))
                    }
                }
                .show(fragmentManager!!, "asdf")
        }

        apartment_parameters_decrease_beds_count.setOnClickListener {
            viewModel.onChangeBedButtonClick(false)
        }

        apartment_parameters_increase_beds_count.setOnClickListener {
            viewModel.onChangeBedButtonClick(true)
        }
    }

    private fun handleDates(dates: String) {
        apartment_parameters_pick_dates_button.text = dates
    }

    private fun handleBedsCount(beds: Int) {
        apartment_parameters_beds_number_label.text = beds.toString()
    }
}
