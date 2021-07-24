package com.taked.stamp_renew.view.main.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.taked.stamp_renew.databinding.FragmentStampBinding
import com.taked.stamp_renew.viewmodel.main.StampViewModel

class StampFragment : Fragment() {

    private val viewModel = StampViewModel()
    private lateinit var binding: FragmentStampBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentStampBinding.inflate(inflater, container, false).apply {
            viewmodel = viewModel
            lifecycleOwner = viewLifecycleOwner
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        viewModel.isButtonClicked.observe(viewLifecycleOwner, { isEnabled ->
//            binding.button.isEnabled = isEnabled
//        })
//
//        binding.inputText.addTextChangedListener { text ->
//            val isInvalid = text.isNullOrBlank() || text.length < 4
//            viewModel.run {
//                updateButton(isInvalid)
//                updateText(isInvalid)
//            }
//        }
    }

}