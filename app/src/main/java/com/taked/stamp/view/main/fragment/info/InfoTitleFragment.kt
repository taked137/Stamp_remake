package com.taked.stamp.view.main.fragment.info

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.taked.stamp.databinding.FragmentInfoBinding
import com.taked.stamp.model.api.APIRepository
import com.taked.stamp.view.info.InfoActivity
import com.taked.stamp.view.quiz.activity.QuizActivity
import com.taked.stamp.viewmodel.main.InfoTitleViewModel
import com.taked.stamp.viewmodel.util.ToastUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class InfoTitleFragment : Fragment() {

    private lateinit var binding: FragmentInfoBinding
    private val titleViewModel: InfoTitleViewModel by viewModels()

    @Inject
    lateinit var apiRepository: APIRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentInfoBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
        }

        val adapter = InfoTitleItemAdapter(InfoTitleItemAdapter.OnClickListener {
            val intent = Intent(requireActivity(), InfoActivity::class.java).apply {
                putExtra("infoID", it.id)
            }
            startActivity(intent)
        })
        binding.recyclerView.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            titleViewModel.samplePagingFlow.collectLatest { pagingData ->
                adapter.submitData(pagingData)
            }
        }

        return binding.root
    }
}
