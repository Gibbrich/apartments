package com.github.gibbrich.airmee.utils

import androidx.fragment.app.Fragment
import com.github.gibbrich.airmee.core.model.BookingRange
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import java.util.*

fun Fragment.showDateRangePicker(onConfirmDatesButtonClick: (BookingRange) -> Unit) {
    val point = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        .also {
            it.set(Calendar.HOUR_OF_DAY, 0)
            it.set(Calendar.MINUTE, 0)
            it.set(Calendar.SECOND, 0)
            it.set(Calendar.MILLISECOND, 0)
        }
        .time
        .time

    val constraints = CalendarConstraints.Builder()
        .setValidator(DateValidatorPointForward.from(point))
        .build()

    MaterialDatePicker.Builder
        .dateRangePicker()
        .setCalendarConstraints(constraints)
        .build()
        .apply {
            addOnPositiveButtonClickListener {
                onConfirmDatesButtonClick.invoke(BookingRange(it.first!!, it.second!!))
            }
        }
        .show(childFragmentManager, "MaterialDatePicker")
}