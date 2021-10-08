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
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.taked.stamp.R
import androidx.activity.result.contract.ActivityResultContracts
import com.taked.stamp.model.api.APIRepository
import com.taked.stamp.viewmodel.util.ToastUtil
import kotlinx.coroutines.runBlocking


class MapFragment : Fragment(), OnMapReadyCallback {
    companion object {
        val NIT = LatLng(35.156893, 136.925268)
    }

    private lateinit var mMap: GoogleMap

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
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.fragment_map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        requestPermission.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(NIT, 17f))

        val checkPoints = runBlocking { APIRepository.getCheckPoint() }!!
        checkPoints.checkpoint.forEach {
            mMap.addMarker(
                MarkerOptions().position(LatLng(it.latitude, it.longitude))
                    .title("謎解き${it.num}のチェックポイント")
            )
        }
    }
}
