package com.github.gibbrich.airmee

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.github.gibbrich.airmee.core.model.Apartment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import androidx.core.app.ActivityCompat.requestPermissions
import android.content.pm.PackageManager
import android.location.Location
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.gibbrich.airmee.core.checkLocationPermission
import com.github.gibbrich.airmee.core.getLocationPermissions
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Marker
import kotlinx.android.synthetic.main.maps_fragment.*


class MapsFragment : Fragment() {
    companion object {
        private const val DEFAULT_ZOOM = 15f
        private const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 42
    }

    private lateinit var viewModel: MapsViewModel
    private lateinit var googleMap: GoogleMap
    private var currentLocationMarker: Marker? = null
    private var adapter: ApartmentsAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.maps_fragment, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MapsViewModel::class.java)
        viewModel.apartments.observe(this, Observer(::handleApartments))
        viewModel.getLocationsSource().observe(this, Observer(::handleLocations))

        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync { map ->
            googleMap = map

            val latLng = viewModel.getUserLocation().let { LatLng(it.latitude, it.longitude) }
            googleMap.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    latLng,
                    DEFAULT_ZOOM
                )
            )
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

        apartments_list.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)

        if (adapter == null) {
            adapter = ApartmentsAdapter(viewModel.getUserLocation(), mutableListOf(), viewModel::onChangeFiltersClick)
        }

        apartments_list.adapter = adapter
    }

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

    private fun handleLocations(location: Location) {
        currentLocationMarker?.remove()

        //Place current location marker
        val latLng = LatLng(location.latitude, location.longitude)
        val markerOptions = MarkerOptions()
        markerOptions.position(latLng)
        markerOptions.title("Current Position")
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA))
        currentLocationMarker = googleMap.addMarker(markerOptions)

        //move map camera
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ZOOM))
    }

    private fun handleApartments(apartments: List<Apartment>) {
        adapter?.let {
            it.items.clear()
            it.items.addAll(apartments)
            it.notifyDataSetChanged()
        }

        apartments
            .map(Apartment::toMarkerOptions)
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

private fun Apartment.toMarkerOptions() = MarkerOptions()
    .position(LatLng(latitude, longitude))
    .title(name)
