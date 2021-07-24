package com.taked.stamp_renew.view.main.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import com.taked.stamp_renew.databinding.FragmentStampBinding
import com.taked.stamp_renew.model.ActivityState
import com.taked.stamp_renew.viewmodel.main.StampViewModel

class StampFragment : Fragment() {

    lateinit var viewModel: StampViewModel
    private lateinit var binding: FragmentStampBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val judgeArray = Array(6) { false }
        val clearArray = Array(6) { false }
        val dataStore =
            requireActivity().getSharedPreferences("DataStore", AppCompatActivity.MODE_PRIVATE)
        for (i in 0..5) {
            judgeArray[i] = dataStore.getBoolean("judge$i", false)
            clearArray[i] = dataStore.getBoolean("clear$i", false)
        }
        viewModel = StampViewModel(judgeArray, clearArray)

        binding = FragmentStampBinding.inflate(inflater, container, false).apply {
            viewmodel = viewModel
            lifecycleOwner = viewLifecycleOwner
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeState(viewModel.judgeInfo, "judge")
        observeState(viewModel.clearInfo, "clear")
    }

    private fun observeState(array: LiveData<Array<Boolean>>, key: String) {
        array.observe(viewLifecycleOwner, { _ ->
            requireActivity().getSharedPreferences(
                "DataStore", AppCompatActivity.MODE_PRIVATE
            ).edit {
                for ((count, bool) in array.value!!.withIndex()) {
                    putBoolean("$key$count", bool)
                }
            }
        })
    }

}