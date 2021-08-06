package com.taked.stamp_renew.view.main.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import com.taked.stamp_renew.R
import com.taked.stamp_renew.databinding.FragmentQuizBinding
import com.taked.stamp_renew.databinding.FragmentTitleBinding
import com.taked.stamp_renew.model.ActivityState
import com.taked.stamp_renew.view.main.StateKeys
import com.taked.stamp_renew.viewmodel.title.TitleViewModel

class QuizFragment : Fragment() {

    private val viewModel = TitleViewModel()
    private lateinit var binding: FragmentQuizBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentQuizBinding.inflate(inflater, container, false).apply {
            viewmodel = viewModel
            lifecycleOwner = viewLifecycleOwner
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.button.setOnClickListener {
            requireActivity().apply {
                getSharedPreferences("DataStore", AppCompatActivity.MODE_PRIVATE)
                    .edit {
                        putInt(StateKeys.PROGRESS.key, ActivityState.GAME.value)
                    }
            }
        }
    }

}