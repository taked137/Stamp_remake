package com.taked.stamp_renew.view.main.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import com.taked.stamp_renew.viewmodel.util.SharedPreferenceUtil.Companion.SharedPreferenceKey
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import com.squareup.moshi.Moshi
import com.taked.stamp_renew.databinding.FragmentStampBinding
import com.taked.stamp_renew.view.main.ActivityState
import com.taked.stamp_renew.viewmodel.util.APIController
import com.taked.stamp_renew.view.main.StateData
import com.taked.stamp_renew.view.main.activity.QuizActivity
import com.taked.stamp_renew.viewmodel.util.AlertUtil
import com.taked.stamp_renew.viewmodel.main.StampViewModel
import com.taked.stamp_renew.viewmodel.util.SharedPreferenceUtil
import kotlinx.coroutines.runBlocking

class StampFragment : Fragment() {

    private lateinit var uuid: String
    private lateinit var viewModel: StampViewModel
    private lateinit var binding: FragmentStampBinding

    private val adapter = Moshi.Builder().build().adapter(StateData::class.java)
    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult? ->
            if (result?.resultCode != Activity.RESULT_OK) {
                return@registerForActivityResult
            }
            result.data?.let { data ->
                updateStamp(data)
            }
        }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        uuid =
            SharedPreferenceUtil.getString(requireActivity(), SharedPreferenceKey.UUID, "")!!
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val positionList = getStateList(SharedPreferenceKey.POSITION)
        val quizList = getStateList(SharedPreferenceKey.QUIZ)

        viewModel = StampViewModel(positionList, quizList).apply {
            observeState(this.judgeInfo, SharedPreferenceKey.POSITION)
            observeState(this.clearInfo, SharedPreferenceKey.QUIZ)

            quizLiveData.observe(viewLifecycleOwner, { quizNum ->
                val intent = Intent(requireActivity(), QuizActivity::class.java).apply {
                    putExtra("quizID", quizNum)
                }
                startForResult.launch(intent)
            })
            imageLiveData.observe(viewLifecycleOwner, { quizNum ->
                if (!AlertUtil.showProgressDialog(requireContext(), "ビーコン取得中...", container)) {
                    return@observe
                }

                val response =
                    runBlocking {
                        APIController.judgeBeacon(uuid, quizNum, listOf(1, 2, 3))
                    }
                if (response == null) {
                    AlertUtil.showNotifyDialog(
                        requireActivity(), title = "通信結果", message = "通信エラーが発生しました。もう一度試してみてください。"
                    )
                    return@observe
                }

                showBeaconAlert(response.id, quizNum)
            })
            goalLiveData.observe(viewLifecycleOwner, {
                val progress =
                    SharedPreferenceUtil.getInt(requireActivity(), SharedPreferenceKey.PROGRESS, -1)
                showGoalAlert(progress)
            })
        }

        binding = FragmentStampBinding.inflate(inflater, container, false).apply {
            viewmodel = viewModel
            lifecycleOwner = viewLifecycleOwner
        }

        return binding.root
    }

    private fun updateStamp(data: Intent) {
        val quizID = data.getIntExtra("correctNum", -1)
        if (quizID < 0) {
            return
        }

        viewModel.updateClearInfo(quizID)
        if (!viewModel.judgeCleared()) {
            return
        }

        val response = runBlocking {
            APIController.postGoal(uuid)
        }
        if (response!!.accept) {
            AlertUtil.showNotifyDialog(
                requireActivity(), "ゲームクリア", "おめでとうございます！ゲームクリアです！"
            )
        }

        SharedPreferenceUtil.putInt(
            requireActivity(), SharedPreferenceKey.PROGRESS, ActivityState.CLEAR.value
        )
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

private fun showBeaconAlert(id: Int, quizNum: Int) {
    when (id) {
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
                callback = { viewModel.onQuizClick(quizNum) }
            )
            viewModel.updateJudgeInfo(quizNum)
        }
    }
}

private fun showGoalAlert(progress: Int) {
    when (progress) {
        ActivityState.CLEAR.value ->
            AlertUtil.showNotifyDialog(
                requireActivity(), "ゲームクリア", "おめでとうございます！ゲームクリアです！"
            )
        ActivityState.GAME.value ->
            AlertUtil.showNotifyDialog(
                requireActivity(), "クリア済み", "クリア済みの問題です。他の問題にチャレンジしてみてください。"
            )
    }
}
}