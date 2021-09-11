package com.taked.stamp.view.main.fragment.info

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.taked.stamp.databinding.FragmentInfoBinding
import com.taked.stamp.viewmodel.main.InfoViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class InfoFragment : Fragment() {

    private lateinit var binding: FragmentInfoBinding
    private val viewModel: InfoViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentInfoBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
        }

        val adapter = InfoItemAdapter()
        binding.recyclerView.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.samplePagingFlow.collectLatest { pagingData ->
                adapter.submitData(pagingData)
            }
        }

        return binding.root
    }
}
