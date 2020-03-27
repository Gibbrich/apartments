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
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.gibbrich.airmee.core.checkLocationPermission
import com.github.gibbrich.airmee.core.getLocationPermissions
import com.github.gibbrich.airmee.model.ApartmentViewData
import com.github.gibbrich.airmee.ui.ApartmentsAdapter
import com.github.gibbrich.airmee.ui.utils.SnapHelperOneByOne
import com.github.gibbrich.airmee.viewModel.MapsViewModel
import kotlinx.android.synthetic.main.maps_fragment.*

// todo - on pin on map click, change recycler item
class MapsFragment : Fragment() {
    companion object {
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

        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this::onMapReady)

        getLocationPermissionIfNeed()

        val layoutManager = LinearLayoutManager(
            activity,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        map_fragment_apartments_list.layoutManager = layoutManager

        if (adapter == null) {
            adapter = ApartmentsAdapter(
                mutableListOf(),
                viewModel::onChangeFiltersClick
            )
        }

        map_fragment_apartments_list.adapter = adapter
        val snapHelper = SnapHelperOneByOne()
        snapHelper.attachToRecyclerView(map_fragment_apartments_list)
        map_fragment_apartments_list.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    val centerView = snapHelper.findSnapView(layoutManager) ?: return
                    val position = layoutManager.getPosition(centerView)
                    viewModel.onScrollEnd(position)
                }
            }
        })

        // todo - fix bug, related to multiple fragments open
        map_fragment_apartments_parameters_button.setOnClickListener {
            fragmentManager?.beginTransaction()
                ?.add(
                    R.id.fragment_apartment_parameters,
                    ApartmentParametersFragment(),
                    "ApartmentParametersFragment"
                )
                ?.addToBackStack("ApartmentParametersFragment")
                ?.commit()
        }

        map_fragment_zoom_in.setOnClickListener {
            viewModel.onZoomChange(true)
        }

        map_fragment_zoom_out.setOnClickListener {
            viewModel.onZoomChange(false)
        }

        map_fragment_current_position.setOnClickListener {
            viewModel.onCurrentLocationButtonClick()
        }
    }

    private fun onMapReady(map: GoogleMap) {
        googleMap = map

        viewModel.apartments.observe(this, Observer(::handleApartments))
        viewModel.cameraPosition.observe(this, Observer(::moveCameraFocus))
        viewModel.cameraZoom.observe(this, Observer(::handleCameraZoom))

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

    private fun handleCameraZoom(zoom: Float) =
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(zoom))

    private fun moveCameraFocus(position: LatLng) = googleMap.animateCamera(
        CameraUpdateFactory.newLatLngZoom(
            position,
            viewModel.cameraZoom.value!!
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

        googleMap.clear()

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
        mMap.isMyLocationEnabled = isLocationPermissionGranted
        mMap.uiSettings.isMyLocationButtonEnabled = false
    }
}

private fun ApartmentViewData.toMarkerOptions() = MarkerOptions()
    .position(LatLng(latitude, longitude))
    .title(name)
