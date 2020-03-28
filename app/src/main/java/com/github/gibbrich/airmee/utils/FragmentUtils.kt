package com.github.gibbrich.airmee.utils

import androidx.fragment.app.Fragment
import com.github.gibbrich.airmee.core.model.BookingRange
import com.google.android.material.datepicker.MaterialDatePicker

fun Fragment.showDateRangePicker(onConfirmDatesButtonClick: (BookingRange) -> Unit) {
    MaterialDatePicker.Builder
        .dateRangePicker()
        .build()
        .apply {
            addOnPositiveButtonClickListener {
                onConfirmDatesButtonClick.invoke(BookingRange(it.first!!, it.second!!))
            }
        }
        .show(childFragmentManager, "MaterialDatePicker")
}