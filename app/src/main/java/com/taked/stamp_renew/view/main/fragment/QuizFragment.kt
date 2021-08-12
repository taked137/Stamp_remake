package com.taked.stamp_renew.view.main.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import coil.load
import com.taked.stamp_renew.databinding.FragmentQuizBinding
import com.taked.stamp_renew.viewmodel.util.APIController
import com.taked.stamp_renew.viewmodel.main.QuizViewModel
import kotlinx.coroutines.launch

class QuizFragment : Fragment() {

    private lateinit var viewModel: QuizViewModel
    private lateinit var binding: FragmentQuizBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val quizID = requireActivity().intent.getIntExtra("quizID", -1)

        viewModel = QuizViewModel(quizID).apply {
            backLiveData.observe(viewLifecycleOwner, {
                requireActivity().finish()
            })
        }

        binding = FragmentQuizBinding.inflate(inflater, container, false).apply {
            viewmodel = viewModel
            lifecycleOwner = viewLifecycleOwner

            inputText.addTextChangedListener { text ->
                viewModel.updateButton(text.isNullOrBlank())
            }

            button.setOnClickListener {
            }

            lifecycleScope.launch {
                val response = APIController.requestQuiz(quizID + 1)!!
                quizImage.load(response.url)
            }
        }

        return binding.root
    }
}