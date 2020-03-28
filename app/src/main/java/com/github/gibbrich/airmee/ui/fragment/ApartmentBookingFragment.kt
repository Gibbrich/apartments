package com.github.gibbrich.airmee.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.github.gibbrich.airmee.viewModel.ApartmentViewModel
import com.github.gibbrich.airmee.viewModel.ApartmentViewModelFactory
import com.github.gibbrich.airmee.R
import com.github.gibbrich.airmee.di.DI
import com.github.gibbrich.airmee.manager.INavigationManager
import com.github.gibbrich.airmee.utils.showDateRangePicker
import kotlinx.android.synthetic.main.apartment_booking_fragment.*
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

        viewModel.bookingRange.observe(this, Observer(::handleBookingRange))

        apartment_booking_select_dates_button.setOnClickListener {
            showDateRangePicker(viewModel::onConfirmDatesButtonClick)
        }

        apartment_booking_name_label.text = viewModel.apartment.name
        apartment_booking_beds_quantity_label.text = viewModel.apartment.bedrooms.toString()

        apartment_booking_book.setOnClickListener {
            viewModel.onBookButtonClick()
            navigationManager.exit()
        }
    }

    private fun handleBookingRange(range: String) {
        apartment_booking_select_dates_button.text = range
    }
}
