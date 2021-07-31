package com.taked.stamp_renew.view.main.fragment

import android.animation.ObjectAnimator
import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.ProgressBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import com.squareup.moshi.Moshi
import com.taked.stamp_renew.R
import com.taked.stamp_renew.databinding.CustomDialogBinding
import com.taked.stamp_renew.databinding.FragmentStampBinding
import com.taked.stamp_renew.model.ActivityState
import com.taked.stamp_renew.view.main.StateData
import com.taked.stamp_renew.view.main.StateKeys
import com.taked.stamp_renew.view.util.AlertUtil
import com.taked.stamp_renew.viewmodel.main.StampViewModel

class StampFragment : Fragment() {

    lateinit var viewModel: StampViewModel
    private lateinit var binding: FragmentStampBinding
    private val adapter = Moshi.Builder().build().adapter(StateData::class.java)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val dataStore =
            requireActivity().getSharedPreferences("DataStore", AppCompatActivity.MODE_PRIVATE)

        val positionList =
            try {
                adapter.fromJson(
                    dataStore.getString(StateKeys.POSITION.key, "")!!
                )!!.hasCleared.toMutableList()
            } catch (e: Exception) {
                MutableList(6) { false }
            }
        val quizList =
            try {
                adapter.fromJson(
                    dataStore.getString(StateKeys.QUIZ.key, "")!!
                )!!.hasCleared.toMutableList()
            } catch (e: Exception) {
                MutableList(6) { false }
            }

        viewModel = StampViewModel(positionList, quizList)

        binding = FragmentStampBinding.inflate(inflater, container, false).apply {
            viewmodel = viewModel
            lifecycleOwner = viewLifecycleOwner
        }

        observeState(viewModel.judgeInfo, StateKeys.POSITION, container)
        observeState(viewModel.clearInfo, StateKeys.QUIZ, container)

        return binding.root
    }

    private fun observeState(
        array: LiveData<List<Boolean>>, stateKey: StateKeys, container: ViewGroup?
    ) {
        array.observe(viewLifecycleOwner, {
            AlertUtil.showProgressDialog(requireContext(), "ビーコン取得中...", container)
            
            val clearArray = MutableList(6) { false }
            for ((count, bool) in array.value!!.withIndex()) {
                clearArray[count] = bool
            }

            requireActivity().getSharedPreferences(
                "DataStore", AppCompatActivity.MODE_PRIVATE
            ).edit {
                putString(stateKey.key, adapter.toJson(StateData(clearArray)))
            }
        })
    }

}