package com.taked.stamp_renew.view.main.fragment

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import com.taked.stamp_renew.viewmodel.util.SharedPreferenceUtil.Companion.SharedPreferenceKey
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import com.squareup.moshi.Moshi
import com.taked.stamp_renew.databinding.FragmentStampBinding
import com.taked.stamp_renew.viewmodel.util.APIController
import com.taked.stamp_renew.view.main.StateData
import com.taked.stamp_renew.view.main.activity.QuizActivity
import com.taked.stamp_renew.viewmodel.util.AlertUtil
import com.taked.stamp_renew.viewmodel.main.StampViewModel
import com.taked.stamp_renew.viewmodel.util.SharedPreferenceUtil
import kotlinx.coroutines.runBlocking

class StampFragment : Fragment() {

    private lateinit var viewModel: StampViewModel
    private lateinit var binding: FragmentStampBinding
    private val adapter = Moshi.Builder().build().adapter(StateData::class.java)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val positionList = getStateList(SharedPreferenceKey.POSITION)
        val quizList = getStateList(SharedPreferenceKey.QUIZ)

        viewModel = StampViewModel(positionList, quizList)

        binding = FragmentStampBinding.inflate(inflater, container, false).apply {
            viewmodel = viewModel
            lifecycleOwner = viewLifecycleOwner
        }

        observeState(viewModel.judgeInfo, SharedPreferenceKey.POSITION)
        observeState(viewModel.clearInfo, SharedPreferenceKey.QUIZ)
        viewModel.apply {
            quizLiveData.observe(viewLifecycleOwner, {
                val intent = Intent(requireActivity(), QuizActivity::class.java).apply {
                    putExtra("quizID", it)
                }
                startActivity(intent)
            })
            imageLiveData.observe(viewLifecycleOwner, {
                if (!AlertUtil.showProgressDialog(requireContext(), "ビーコン取得中...", container)) {
                    return@observe
                }

                val response =
                    runBlocking {
                        APIController.judgeBeacon(1, listOf(1, 2, 3))
                    }

                when (response!!.id) {
                    // 範囲外
                    0 -> AlertUtil.showNotifyDialog(
                        requireActivity(), title = "取得結果", message = "範囲外です。"
                    )
                    // 範囲付近
                    1 -> AlertUtil.showNotifyDialog(
                        requireActivity(), title = "取得結果", message = "範囲付近です。もう少し近づいてください。"
                    )
                    // 範囲内
                    2 -> {
                        AlertUtil.showNotifyDialog(
                            requireActivity(), title = "取得結果", message = "範囲内です。問題を表示します。",
                            callback = { this.onClick(it) }
                        )
                        updateJudgeInfo(it)
                    }
                }
            })
        }

        return binding.root
    }

    private fun observeState(
        array: LiveData<List<Boolean>>, sharedPreferenceKey: SharedPreferenceKey
    ) {
        array.observe(viewLifecycleOwner, {
            val clearArray = MutableList(6) { false }
            for ((count, bool) in array.value!!.withIndex()) {
                clearArray[count] = bool
            }

            SharedPreferenceUtil.putString(
                requireActivity(), sharedPreferenceKey, adapter.toJson(StateData(clearArray))
            )
        })
    }

    private fun getStateList(sharedPreferenceKey: SharedPreferenceKey) =
        try {
            val data = SharedPreferenceUtil.getString(requireActivity(), sharedPreferenceKey, "")
            adapter.fromJson(data!!)!!.hasCleared.toMutableList()
        } catch (e: Exception) {
            MutableList(6) { false }
        }

}