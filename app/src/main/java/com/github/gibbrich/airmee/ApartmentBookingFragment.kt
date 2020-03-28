package com.github.gibbrich.airmee

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.github.gibbrich.airmee.core.model.BookingRange
import com.github.gibbrich.airmee.utils.showDateRangePicker
import com.google.android.material.datepicker.MaterialDatePicker
import kotlinx.android.synthetic.main.apartment_booking_fragment.*


class ApartmentBookingFragment : Fragment() {
    private val viewModel: ApartmentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.apartment_booking_fragment, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.bookingRange.observe(this, Observer(::handleBookingRange))

        apartment_booking_select_dates_button.setOnClickListener {
            showDateRangePicker(viewModel::onConfirmDatesButtonClick)
        }
    }

    private fun handleBookingRange(range: String) {
        apartment_booking_select_dates_button.text = range
    }
}
