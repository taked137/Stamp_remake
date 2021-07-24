package com.taked.stamp_renew.ui.fragment

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import com.taked.stamp_renew.R
import com.taked.stamp_renew.databinding.RegisterFragmentBinding
import com.taked.stamp_renew.ui.activity.MainActivity
import com.taked.stamp_renew.viewmodel.RegisterViewModel

class RegisterFragment : Fragment() {

    private val viewModel = RegisterViewModel()
    private lateinit var binding: RegisterFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = RegisterFragmentBinding.inflate(inflater, container, false).apply {
            viewmodel = viewModel
            lifecycleOwner = viewLifecycleOwner
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.isButtonEnabled.observe(viewLifecycleOwner, { isEnabled ->
            binding.button.isEnabled = isEnabled
        })

        binding.apply {
            inputText.addTextChangedListener { text ->
                val isInvalid = text.isNullOrBlank() || text.length < 4
                viewModel.run {
                    updateButton(isInvalid)
                    updateText(isInvalid)
                }
            }
            button.setOnClickListener {
                val intent = Intent(requireActivity(), MainActivity::class.java).apply {
                    addFlags(FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                startActivity(intent)
            }
        }

    }

}