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
import android.content.pm.PackageManager
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.gibbrich.airmee.R
import com.github.gibbrich.airmee.core.utils.isLocationPermissionGranted
import com.github.gibbrich.airmee.core.utils.getLocationPermissions
import com.github.gibbrich.airmee.manager.INavigationManager
import com.github.gibbrich.airmee.di.DI
import com.github.gibbrich.airmee.model.ApartmentViewData
import com.github.gibbrich.airmee.model.CameraProperties
import com.github.gibbrich.airmee.adapter.ApartmentsAdapter
import com.github.gibbrich.airmee.utils.SnapHelperOneByOne
import com.github.gibbrich.airmee.utils.redraw
import com.github.gibbrich.airmee.utils.getErrorView
import com.github.gibbrich.airmee.viewModel.MapsViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.maps_fragment.*
import javax.inject.Inject

class MapsFragment : Fragment() {
    companion object {
        private const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 42
    }

    @Inject
    internal lateinit var navigationManager: INavigationManager

    private val viewModel: MapsViewModel by activityViewModels()
    private lateinit var googleMap: GoogleMap

    private var adapter: ApartmentsAdapter? = null
    private var errorView: Snackbar? = null

    init {
        DI.appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.maps_fragment, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.error.observe(viewLifecycleOwner, Observer(::handleErrorSignal))
        viewModel.fetchApartmentsIfNeed()

        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this::onMapReady)

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
                navigationManager::switchToApartmentBookingScreen,
                viewModel::onApartmentLongClick
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

        viewModel.onDestroyView()
        errorView?.dismiss()
    }

    private fun onMapReady(map: GoogleMap) {
        googleMap = map
        map.uiSettings.isMyLocationButtonEnabled = false
        map.setOnMarkerClickListener {
            val position = viewModel.onMapMarkerClick(it.tag as Int)
            map_fragment_apartments_list.smoothScrollToPosition(position)
            false
        }

        viewModel.apartments.observe(viewLifecycleOwner, Observer(::handleApartments))
        viewModel.cameraProperties.observe(viewLifecycleOwner, Observer(::handleCameraPropertiesSource))

        activity?.let {
            if (isLocationPermissionGranted(it)) {
                updateLocationUI(true)
            } else {
                requestPermissions(
                    getLocationPermissions(),
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
                )
            }
        }
    }

    private fun handleCameraPropertiesSource(cameraProperties: CameraProperties) {
        val cameraUpdate = if (cameraProperties.shouldMove) {
            CameraUpdateFactory.newLatLngZoom(
                cameraProperties.latLng,
                cameraProperties.zoom
            )
        } else {
            CameraUpdateFactory.zoomTo(cameraProperties.zoom)
        }

        googleMap.animateCamera(cameraUpdate)
    }

    override fun onResume() {
        super.onResume()

        if (isLocationPermissionGranted(activity!!)) {
            viewModel.startFetchingLocation()
        }
    }

    override fun onPause() {
        super.onPause()

        if (isLocationPermissionGranted(activity!!)) {
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
        map_fragment_apartments_list.visibility = View.VISIBLE

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

    private fun handleErrorSignal(isError: Boolean) {
        if (isError) {
            errorView = getErrorView(
                map_fragment_root,
                Snackbar.LENGTH_INDEFINITE,
                R.string.apartments_list_error,
                R.string.apartments_list_retry,
                viewModel::onRetryButtonClick
            ).also {
                it.show()
            }
        } else {
            errorView?.dismiss()
        }
    }

    private fun updateLocationUI(isLocationPermissionGranted: Boolean) {
        googleMap.isMyLocationEnabled = isLocationPermissionGranted
        map_fragment_current_position.visibility =
            if (isLocationPermissionGranted) View.VISIBLE else View.GONE

        if (isLocationPermissionGranted) {
            // apartments card include distance to user. If there is no location permission,
            // we hide it. Once we get it, we should display it, so we trigger recycler redraw.
            map_fragment_apartments_list.redraw()
        }
    }
}

private fun ApartmentViewData.toMarkerOptions() = MarkerOptions()
    .position(LatLng(latitude, longitude))
    .title(name)
