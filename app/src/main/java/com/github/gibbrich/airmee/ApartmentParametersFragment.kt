package com.github.gibbrich.airmee


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.datepicker.MaterialDatePicker
import kotlinx.android.synthetic.main.fragment_apartment_parameters.*

/**
 * A simple [Fragment] subclass.
 */
class ApartmentParametersFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_apartment_parameters, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        apartment_parameters_pick_dates_button.setOnClickListener {
            MaterialDatePicker.Builder
                .dateRangePicker()
                .build()
                .apply {
                    addOnPositiveButtonClickListener {

                    }
                }
                .show(fragmentManager!!, "asdf")
        }
    }
}
