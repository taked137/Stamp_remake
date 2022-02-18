package com.taked.stamp.view.main.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import com.taked.stamp.viewmodel.util.SharedPreferenceUtil.SharedPreferenceKey
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import com.squareup.moshi.Moshi
import com.taked.stamp.databinding.FragmentStampBinding
import com.taked.stamp.view.main.ActivityState
import com.taked.stamp.model.api.APIRepository
import com.taked.stamp.view.main.StateData
import com.taked.stamp.view.quiz.activity.QuizActivity
import com.taked.stamp.viewmodel.util.AlertUtil
import com.taked.stamp.viewmodel.main.StampViewModel
import com.taked.stamp.viewmodel.util.SharedPreferenceUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class StampFragment : Fragment() {

    @Inject
    lateinit var apiRepository: APIRepository

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

    private val positionList: MutableList<Boolean> by lazy { getStateList(SharedPreferenceKey.POSITION) }
    private val quizList: MutableList<Boolean> by lazy { getStateList(SharedPreferenceKey.QUIZ) }
    private val uuid: String by lazy {
        SharedPreferenceUtil.getString(requireActivity(), SharedPreferenceKey.UUID, "")!!
    }
    private val viewModel: StampViewModel by viewModels {
        StampViewModel.Factory(positionList, quizList)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        viewModel.apply {
            observeState(this.judgeInfo, SharedPreferenceKey.POSITION)
            observeState(this.clearInfo, SharedPreferenceKey.QUIZ)

            quizLiveData.observe(viewLifecycleOwner) { quizNum ->
                val intent = Intent(requireActivity(), QuizActivity::class.java).apply {
                    putExtra("quizID", quizNum)
                }
                startForResult.launch(intent)
            }
            imageLiveData.observe(viewLifecycleOwner) { quizNum ->
                if (!AlertUtil.showProgressDialog(requireContext(), "ビーコン取得中...", container)) {
                    return@observe
                }

                val response =
                    runBlocking {
                        apiRepository.judgeBeacon(uuid, quizNum, listOf(1, 2, 3))
                    }
                if (response == null) {
                    AlertUtil.showNotifyDialog(
                        requireActivity(), title = "通信結果", message = "通信エラーが発生しました。もう一度試してみてください。"
                    )
                    return@observe
                }

                showBeaconAlert(response.id, quizNum)
            }
            goalLiveData.observe(viewLifecycleOwner) {
                val progress =
                    SharedPreferenceUtil.getInt(requireActivity(), SharedPreferenceKey.PROGRESS, -1)
                showGoalAlert(progress)
            }
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
            apiRepository.postGoal(uuid)
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
        array.observe(viewLifecycleOwner) {
            val clearArray = MutableList(6) { false }
            for ((count, bool) in array.value!!.withIndex()) {
                clearArray[count] = bool
            }

            SharedPreferenceUtil.putString(
                requireActivity(), sharedPreferenceKey, adapter.toJson(StateData(clearArray))
            )
        }
    }

    private fun getStateList(sharedPreferenceKey: SharedPreferenceKey) =
        try {
            val data = SharedPreferenceUtil.getString(requireActivity(), sharedPreferenceKey, "")
            adapter.fromJson(data!!)!!.hasCleared.toMutableList()
        } catch (_: Exception) {
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
