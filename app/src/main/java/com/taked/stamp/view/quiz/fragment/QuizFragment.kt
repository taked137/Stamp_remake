package com.taked.stamp.view.quiz.fragment

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import coil.load
import com.taked.stamp.databinding.FragmentQuizBinding
import com.taked.stamp.model.api.APIRepository
import com.taked.stamp.viewmodel.quiz.QuizViewModel
import com.taked.stamp.viewmodel.util.AlertUtil
import com.taked.stamp.viewmodel.util.SharedPreferenceUtil
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import com.taked.stamp.viewmodel.util.SharedPreferenceUtil.SharedPreferenceKey
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class QuizFragment(private val quizID: Int) : Fragment() {

    @Inject
    lateinit var apiRepository: APIRepository

    private lateinit var binding: FragmentQuizBinding

    private val uuid: String by lazy {
        SharedPreferenceUtil.getString(requireActivity(), SharedPreferenceKey.UUID, "")!!
    }
    private val viewModel: QuizViewModel by viewModels {
        QuizViewModel.Factory(quizID)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentQuizBinding.inflate(inflater, container, false).apply {
            viewmodel = viewModel
            lifecycleOwner = viewLifecycleOwner

            inputText.addTextChangedListener { text ->
                viewModel.updateButton(text.isNullOrBlank())
            }

            button.setOnClickListener {
                runBlocking {
                    apiRepository.judgeAnswer(uuid, quizID, inputText.text.toString())
                }?.let {
                    if (!it.correct) {
                        AlertUtil.showNotifyDialog(requireActivity(), "解答結果", "不正解！もう一度考えてみてください。")
                        return@let
                    }

                    AlertUtil.showNotifyDialog(requireActivity(), "解答結果", "正解！スタンプを押します！") {
                        requireActivity().apply {
                            val intent = intent.apply {
                                putExtra("correctNum", quizID)
                            }
                            setResult(Activity.RESULT_OK, intent)
                            finish()
                        }
                    }
                }
            }

            lifecycleScope.launch {
                val response = apiRepository.requestQuiz(uuid, quizID)
                response?.let {
                    quizImage.load(it.url)
                }
            }
        }

        return binding.root
    }
}
