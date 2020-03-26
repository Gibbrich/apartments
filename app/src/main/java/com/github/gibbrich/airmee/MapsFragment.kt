package com.github.gibbrich.airmee

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import androidx.core.app.ActivityCompat.requestPermissions
import android.content.pm.PackageManager
import android.util.Log
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.github.gibbrich.airmee.core.checkLocationPermission
import com.github.gibbrich.airmee.core.getLocationPermissions
import com.github.gibbrich.airmee.model.ApartmentViewData
import com.github.gibbrich.airmee.ui.ApartmentsAdapter
import com.github.gibbrich.airmee.ui.utils.SnapHelperOneByOne
import kotlinx.android.synthetic.main.maps_fragment.*


class MapsFragment : Fragment() {
    companion object {
        private const val DEFAULT_ZOOM = 15f
        private const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 42
    }

    private val viewModel: MapsViewModel by viewModels()
    private lateinit var googleMap: GoogleMap
    private var adapter: ApartmentsAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.maps_fragment, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.apartments.observe(this, Observer(::handleApartments))
        viewModel.cameraPosition.observe(this, Observer(::moveCameraFocus))

        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync { map ->
            googleMap = map

            val latLng = viewModel.getUserLocation().let { LatLng(it.latitude, it.longitude) }
            moveCameraFocus(latLng)

            viewModel.getApartments()


            activity?.let {
                if (checkLocationPermission(it)) {
                    updateLocationUI(true)
                } else {
                    requestPermissions(
                        it,
                        getLocationPermissions(),
                        PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
                    )
                }
            }
        }

        getLocationPermissionIfNeed()

        val layoutManager = LinearLayoutManager(
            activity,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        apartments_list.layoutManager = layoutManager

        if (adapter == null) {
            adapter = ApartmentsAdapter(
                mutableListOf(),
                viewModel::onChangeFiltersClick
            )
        }

        apartments_list.adapter = adapter
        val snapHelper = SnapHelperOneByOne()
        snapHelper.attachToRecyclerView(apartments_list)
        apartments_list.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    val centerView = snapHelper.findSnapView(layoutManager) ?: return
                    val position = layoutManager.getPosition(centerView)
                    viewModel.onScrollEnd(position)
                }
            }
        })
    }

    private fun moveCameraFocus(position: LatLng) = googleMap.animateCamera(
        CameraUpdateFactory.newLatLngZoom(
            position,
            DEFAULT_ZOOM
        )
    )

    override fun onResume() {
        super.onResume()

        if (checkLocationPermission(activity!!)) {
            viewModel.startFetchingLocation()
        }
    }

    override fun onPause() {
        super.onPause()

        if (checkLocationPermission(activity!!)) {
            viewModel.stopFetchingLocation()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {
                val isLocationPermissionGranted =
                    grantResults.isNotEmpty() && grantResults.first() == PackageManager.PERMISSION_GRANTED
                viewModel.startFetchingLocation()
                updateLocationUI(isLocationPermissionGranted)
            }
        }
    }

    private fun handleApartments(apartments: List<ApartmentViewData>) {
        adapter?.let {
            it.items.clear()
            it.items.addAll(apartments)
            it.notifyDataSetChanged()
        }

        apartments
            .map(ApartmentViewData::toMarkerOptions)
            .forEach { googleMap.addMarker(it) }
    }

    private fun getLocationPermissionIfNeed() {
        val activity = activity ?: return
        if (checkLocationPermission(activity).not()) {
            requestPermissions(
                activity,
                getLocationPermissions(),
                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
            )
        }
    }

    private fun updateLocationUI(isLocationPermissionGranted: Boolean) {
        val mMap = googleMap
        if (isLocationPermissionGranted) {
            mMap.isMyLocationEnabled = true
            mMap.uiSettings.isMyLocationButtonEnabled = true
        } else {
            mMap.isMyLocationEnabled = false
            mMap.uiSettings.isMyLocationButtonEnabled = false
        }
    }
}

private fun ApartmentViewData.toMarkerOptions() = MarkerOptions()
    .position(LatLng(latitude, longitude))
    .title(name)
