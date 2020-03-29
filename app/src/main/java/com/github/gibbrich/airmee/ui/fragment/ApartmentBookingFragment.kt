package com.github.gibbrich.airmee.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.github.gibbrich.airmee.viewModel.ApartmentViewModel
import com.github.gibbrich.airmee.viewModel.ApartmentViewModelFactory
import com.github.gibbrich.airmee.R
import com.github.gibbrich.airmee.core.model.BookingRange
import com.github.gibbrich.airmee.di.DI
import com.github.gibbrich.airmee.manager.INavigationManager
import com.github.gibbrich.airmee.utils.DebouncedOnClickListener
import com.github.gibbrich.airmee.utils.showDateRangePicker
import com.github.gibbrich.airmee.viewModel.MapsViewModel
import kotlinx.android.synthetic.main.apartment_booking_fragment.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


class ApartmentBookingFragment : Fragment() {
    companion object {
        private const val TAG = "ApartmentBookingFragment"
        private const val APARTMENT_ID = "$TAG.APARTMENT_ID"

        fun getParams(apartmentId: Int) = Bundle().apply {
            putInt(APARTMENT_ID, apartmentId)
        }
    }

    @Inject
    lateinit var navigationManager: INavigationManager

    private val apartmentId by lazy {
        arguments?.getInt(APARTMENT_ID) ?: throw Exception("No Apartment id passed as argument")
    }

    private val viewModel: ApartmentViewModel by viewModels {
        ApartmentViewModelFactory(apartmentId)
    }

    init {
        DI.appComponent.inject(this)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.apartment_booking_fragment, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.bookingRange.observe(viewLifecycleOwner, Observer(::handleBookingRange))

        apartment_booking_select_dates_button.setOnClickListener(DebouncedOnClickListener(500) {
            showDateRangePicker(viewModel::onConfirmDatesButtonClick)
        })

        apartment_booking_name_label.text = viewModel.apartment.name
        apartment_booking_beds_quantity_label.text = viewModel.apartment.bedrooms.toString()

        apartment_booking_book_button.setOnClickListener {
            viewModel.onBookButtonClick()
            navigationManager.exit()
        }

        apartment_booking_close_button.setOnClickListener {
            navigationManager.exit()
        }
    }

    private fun handleBookingRange(bookingRange: BookingRange?) {
        apartment_booking_select_dates_button.text = getRangeRepresentation(bookingRange)
        apartment_booking_book_button.isEnabled = bookingRange != null
    }

    private fun getRangeRepresentation(bookingRange: BookingRange?): String =
        if (bookingRange == null) {
            getString(R.string.apartment_booking_select_dates)
        } else {
            val formatter = SimpleDateFormat("dd MMM", Locale.getDefault())
            "${formatter.format(Date(bookingRange.start))} - ${formatter.format(Date(bookingRange.end))}"
        }
}
