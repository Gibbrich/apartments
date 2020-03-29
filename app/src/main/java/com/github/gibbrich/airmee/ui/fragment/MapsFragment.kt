package com.github.gibbrich.airmee.ui.fragment

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
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.gibbrich.airmee.R
import com.github.gibbrich.airmee.core.checkLocationPermission
import com.github.gibbrich.airmee.core.getLocationPermissions
import com.github.gibbrich.airmee.manager.INavigationManager
import com.github.gibbrich.airmee.di.DI
import com.github.gibbrich.airmee.model.ApartmentViewData
import com.github.gibbrich.airmee.model.CameraProperties
import com.github.gibbrich.airmee.ui.ApartmentsAdapter
import com.github.gibbrich.airmee.ui.utils.SnapHelperOneByOne
import com.github.gibbrich.airmee.viewModel.MapsViewModel
import kotlinx.android.synthetic.main.maps_fragment.*
import javax.inject.Inject

// todo - on pin on map click, change recycler item
class MapsFragment : Fragment() {
    companion object {
        private const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 42
    }

    @Inject
    internal lateinit var navigationManager: INavigationManager

    private val viewModel: MapsViewModel by activityViewModels()
    private lateinit var googleMap: GoogleMap
    private var adapter: ApartmentsAdapter? = null

    init {
        DI.appComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.fetchApartments()
    }

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
                navigationManager::switchToApartmentsParametersScreen,
                navigationManager::switchToApartmentBookingScreen
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

        map_fragment_apartments_parameters_button.setOnClickListener {
            navigationManager.switchToApartmentsParametersScreen()
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

    override fun onDestroyView() {
        super.onDestroyView()

        // after navigation to another screens and returning back
        // we don't want camera change it's location
        viewModel.resetCameraMovement()
    }

    private fun onMapReady(map: GoogleMap) {
        googleMap = map
        googleMap.setOnMarkerClickListener {
            val position = viewModel.onMapMarkerClick(it.tag as Int)
            map_fragment_apartments_list.smoothScrollToPosition(position)
            false
        }

        viewModel.apartments.observe(viewLifecycleOwner, Observer(::handleApartments))
        viewModel.cameraProperties.observe(viewLifecycleOwner, Observer(::handleCameraPropertiesSource))

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

    private fun handleCameraPropertiesSource(cameraProperties: CameraProperties) {
        if (cameraProperties.shouldAnimate.not()) {
            return
        }

        googleMap.animateCamera(
            CameraUpdateFactory.newLatLngZoom(
                cameraProperties.latLng,
                cameraProperties.zoom
            )
        )
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

    private fun handleApartments(apartments: List<ApartmentViewData>) {
        adapter?.let {
            it.items = apartments.toMutableList()
            it.notifyDataSetChanged()
        }

        googleMap.clear()

        apartments
            .associate { it.id to it.toMarkerOptions() }
            .forEach { entry ->
                googleMap
                    .addMarker(entry.value)
                    .also {
                        it.tag = entry.key
                    }
            }
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
