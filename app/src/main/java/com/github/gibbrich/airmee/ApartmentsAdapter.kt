package com.github.gibbrich.airmee

import android.location.Location
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.github.gibbrich.airmee.core.model.Apartment
import kotlinx.android.synthetic.main.apartment_list_empty.view.*
import kotlinx.android.synthetic.main.apartment_list_item.view.*

class ApartmentsAdapter(
    var userLocation: Location,
    var items: MutableList<Apartment>,
    private val onChangeFiltersClick: () -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        private const val VIEW_TYPE_ITEM = 0
        private const val VIEW_TYPE_NO_ITEMS = 1
    }

    private var footerViewHolder: FooterViewHolder? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            VIEW_TYPE_ITEM -> {
                val view = LayoutInflater
                    .from(parent.context)
                    .inflate(R.layout.apartment_list_item, parent, false)

                ApartmentViewHolder(
                    view,
                    view.apartment_list_item_beds_number,
                    view.apartment_list_item_title,
                    view.apartment_list_item_distance
                )
            }

            VIEW_TYPE_NO_ITEMS -> {
                val view = LayoutInflater
                    .from(parent.context)
                    .inflate(R.layout.apartment_list_empty, parent, false)

                val footerViewHolder = FooterViewHolder(view, onChangeFiltersClick)
                this.footerViewHolder = footerViewHolder
                footerViewHolder
            }

            else -> {
                throw NotImplementedError("Not implemented apartments view type")
            }
        }

    override fun getItemCount(): Int = if (shouldShowFooter()) 1 else items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) =
        when (getItemViewType(position)) {
            VIEW_TYPE_ITEM -> {
                val viewHolder = holder as ApartmentViewHolder
                val apartment = items[position]
                val context = viewHolder.itemView.context
                viewHolder.bedsNumberLabel.text = context.getString(
                    R.string.apartments_list_item_beds_number,
                    apartment.bedrooms
                )
                viewHolder.titleLabel.text = apartment.name

                val distanceInMeters = Location("")
                    .apply {
                        latitude = apartment.latitude
                        longitude = apartment.longitude
                    }
                    .distanceTo(userLocation)

                viewHolder.distanceToLabel.text = context.getString(R.string.apartments_list_item_distance, distanceInMeters)
            }

            else -> Unit
        }

    override fun getItemViewType(position: Int): Int = if (shouldShowFooter()) {
        VIEW_TYPE_NO_ITEMS
    } else {
        VIEW_TYPE_ITEM
    }

    private fun shouldShowFooter() = items.isEmpty()
}

private class ApartmentViewHolder(
    view: View,
    val bedsNumberLabel: TextView,
    val titleLabel: TextView,
    val distanceToLabel: TextView
) : RecyclerView.ViewHolder(view)

private class FooterViewHolder(
    view: View,
    val onChangeFiltersClick: () -> Unit
) : RecyclerView.ViewHolder(view) {

    init {
        view.apartments_list_change_filters.setOnClickListener { onChangeFiltersClick.invoke() }
    }
}