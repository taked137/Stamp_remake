package com.taked.stamp_renew.view.main.fragment

import android.animation.ObjectAnimator
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.lifecycleScope
import com.squareup.moshi.Moshi
import com.taked.stamp_renew.databinding.FragmentStampBinding
import com.taked.stamp_renew.model.api.APIController
import com.taked.stamp_renew.view.main.StateData
import com.taked.stamp_renew.view.main.StateKeys
import com.taked.stamp_renew.view.util.AlertUtil
import com.taked.stamp_renew.viewmodel.main.StampViewModel
import kotlinx.coroutines.launch

class StampFragment : Fragment() {

    private lateinit var viewModel: StampViewModel
    private lateinit var binding: FragmentStampBinding
    private val adapter = Moshi.Builder().build().adapter(StateData::class.java)

    private val showProgressDialog: () -> (Unit) = {
        AlertUtil.showProgressDialog(requireContext(), "ビーコン取得中...", binding.root as ViewGroup)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val dataStore =
            requireActivity().getSharedPreferences("DataStore", AppCompatActivity.MODE_PRIVATE)

        val positionList = getStateList(dataStore, StateKeys.POSITION)
        val quizList = getStateList(dataStore, StateKeys.QUIZ)

        viewModel = StampViewModel(positionList, quizList, showProgressDialog)

        binding = FragmentStampBinding.inflate(inflater, container, false).apply {
            viewmodel = viewModel
            lifecycleOwner = viewLifecycleOwner
        }

        observeState(viewModel.judgeInfo, StateKeys.POSITION, container)
        observeState(viewModel.clearInfo, StateKeys.QUIZ, container)


//        lifecycleScope.launch {
//            val a = APIController.requestQuiz(1, listOf(1, 2, 3))
//            Log.e("hello", a.toString())
//        }

        return binding.root
    }

    private fun observeState(
        array: LiveData<List<Boolean>>, stateKey: StateKeys, container: ViewGroup?
    ) {
        array.observe(viewLifecycleOwner, {
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

    private fun getStateList(dataStore: SharedPreferences, state: StateKeys) =
        try {
            adapter.fromJson(
                dataStore.getString(state.key, "")!!
            )!!.hasCleared.toMutableList()
        } catch (e: Exception) {
            MutableList(6) { false }
        }

}