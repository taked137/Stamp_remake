package com.taked.stamp.viewmodel.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.taked.stamp.model.api.APIRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(private val apiRepository: APIRepository) : ViewModel() {
    companion object {
        val NIT = LatLng(35.156893, 136.925268)
    }

    private val _checkPoints: MutableLiveData<List<MarkerOptions>> =
        MutableLiveData<List<MarkerOptions>>()
    val checkPoints: LiveData<List<MarkerOptions>>
        get() = _checkPoints

    fun updateCheckPoint() {
        val points = runBlocking { apiRepository.getCheckPoint()!!.checkpoint }

        _checkPoints.value = points.map { checkPoint ->
            MarkerOptions().position(LatLng(checkPoint.latitude, checkPoint.longitude))
                .title("謎解き${checkPoint.num}のチェックポイント")
        }
    }
}
