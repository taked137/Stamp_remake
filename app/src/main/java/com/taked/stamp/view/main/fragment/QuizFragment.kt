package com.taked.stamp.view.main.fragment

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import coil.load
import com.taked.stamp.databinding.FragmentQuizBinding
import com.taked.stamp.model.api.APIController
import com.taked.stamp.viewmodel.main.QuizViewModel
import com.taked.stamp.viewmodel.util.AlertUtil
import com.taked.stamp.viewmodel.util.SharedPreferenceUtil
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import com.taked.stamp.viewmodel.util.SharedPreferenceUtil.Companion.SharedPreferenceKey

class QuizFragment(private val quizID: Int) : Fragment() {

    private lateinit var uuid: String
    private lateinit var viewModel: QuizViewModel
    private lateinit var binding: FragmentQuizBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        uuid =
            SharedPreferenceUtil.getString(requireActivity(), SharedPreferenceKey.UUID, "")!!
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        viewModel = QuizViewModel(quizID)
        binding = FragmentQuizBinding.inflate(inflater, container, false).apply {
            viewmodel = viewModel
            lifecycleOwner = viewLifecycleOwner

            inputText.addTextChangedListener { text ->
                viewModel.updateButton(text.isNullOrBlank())
            }

            button.setOnClickListener {
                runBlocking {
                    APIController.judgeAnswer(uuid, quizID, inputText.text.toString())
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
                val response = APIController.requestQuiz(uuid, quizID)
                response?.let {
                    quizImage.load(it.url)
                }
            }
        }

        return binding.root
    }
}
