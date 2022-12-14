package com.example.storyappfinal.ui.main.explore

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.storyappfinal.data.viewmodel.StoryViewModel
import com.example.storyappfinal.R
import com.example.storyappfinal.data.model.Story
import com.example.storyappfinal.data.repository.remote.ApiConfig
import com.example.storyappfinal.databinding.CustomDialogInfoBinding
import com.example.storyappfinal.databinding.FragmentExploreBinding
import com.example.storyappfinal.ui.detail.DetailActivity
import com.example.storyappfinal.ui.main.MainActivity
import com.example.storyappfinal.utils.Constanta
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class ExploreFragment : Fragment(), OnMapReadyCallback, GoogleMap.InfoWindowAdapter{
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var mMap: GoogleMap
    private lateinit var binding: FragmentExploreBinding
    private val storyViewModel: StoryViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentExploreBinding.inflate(inflater, container, false)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        val mapFragment =
            (childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment)
        mapFragment.getMapAsync(this)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true
        mMap.uiSettings.isTiltGesturesEnabled = true

        storyViewModel.storyList.observe(viewLifecycleOwner) { storyList ->
            for (story in storyList) {
                mMap.addMarker(
                    MarkerOptions().position(
                        LatLng(
                            story.lat?.toDouble() ?: 0.0,
                            story.lon?.toDouble() ?: 0.0
                        )
                    )
                )?.tag = story
            }
        }
        mMap.setInfoWindowAdapter(this)
        mMap.setOnInfoWindowClickListener { marker ->
            val data: Story = marker.tag as Story
            routeToDetailStory(data)
        }
        storyViewModel.loadStoryLocationData(
            requireContext(),
            (activity as MainActivity).getUserToken(), ApiConfig.getApiService()
        )
        getMyLocation()

    }

    private fun routeToDetailStory(data: Story) {
        val intent = Intent(requireContext(), DetailActivity::class.java)
        intent.putExtra(Constanta.StoryDetail.UserName.name, data.name)
        intent.putExtra(Constanta.StoryDetail.ImageURL.name, data.photoUrl)
        intent.putExtra(
            Constanta.StoryDetail.ContentDescription.name,
            data.description
        )
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        requireContext().startActivity(intent)
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                getMyLocation()
            }
        }

    private fun getMyLocation() {
        if (ContextCompat.checkSelfPermission(
                requireContext().applicationContext,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    val latLng = LatLng(location.latitude, location.longitude)
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 8f))
                }
            }
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
        }
    }

    override fun getInfoContents(p0: Marker): View? {
        return null
    }

    override fun getInfoWindow(marker: Marker): View? {
        val bindingTooltips =
            CustomDialogInfoBinding.inflate(LayoutInflater.from(requireContext()))
        val data: Story = marker.tag as Story
        bindingTooltips.message.text = "Story By " + data.name
        return bindingTooltips.root
    }
}

