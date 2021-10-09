package com.taked.stamp.view.main.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import com.taked.stamp.databinding.FragmentMapBinding
import com.taked.stamp.viewmodel.main.MapViewModel
import com.taked.stamp.viewmodel.util.ToastUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: FragmentMapBinding
    private val viewModel: MapViewModel by viewModels()

    @SuppressLint("MissingPermission")
    private val requestPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { result ->
            if (result) {
                mMap.isMyLocationEnabled = true
            } else {
                ToastUtil.makeBottomToast(requireContext(), "現在地が表示されません")
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentMapBinding.inflate(inflater, container, false)
        val mapFragment: SupportMapFragment =
            childFragmentManager.findFragmentById(binding.fragmentMap.id) as SupportMapFragment
        mapFragment.getMapAsync(this)

        viewModel.checkPoints.observe(viewLifecycleOwner, { list ->
            list.forEach { checkPoint ->
                mMap.addMarker(checkPoint)
            }
        })

        return binding.root
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        requestPermission.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(MapViewModel.NIT, 17f))

        viewModel.updateCheckPoint()
    }
}
