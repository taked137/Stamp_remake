package com.taked.stamp.view.title.fragment

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import com.taked.stamp.databinding.FragmentRegisterBinding
import com.taked.stamp.view.main.ActivityState
import com.taked.stamp.model.api.APIRepository
import com.taked.stamp.view.main.activity.MainActivity
import com.taked.stamp.viewmodel.title.RegisterViewModel
import com.taked.stamp.viewmodel.util.SharedPreferenceUtil
import com.taked.stamp.viewmodel.util.SharedPreferenceUtil.SharedPreferenceKey
import kotlinx.coroutines.runBlocking

class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(inflater, container, false).apply {
            viewmodel = viewModel
            lifecycleOwner = viewLifecycleOwner

            inputText.addTextChangedListener { text ->
                val isInvalid = text.isNullOrBlank() || text.length < 4
                viewModel.run {
                    updateButton(isInvalid)
                    updateText(isInvalid)
                }
            }

            button.setOnClickListener {
                val name = inputText.text.toString()
                val device = "${android.os.Build.DEVICE} ${android.os.Build.MODEL}"
                val version = "Android ${android.os.Build.VERSION.RELEASE}"

                val uuid = getUUID(name, device, version)
                SharedPreferenceUtil.putInt(
                    requireActivity(), SharedPreferenceKey.PROGRESS, ActivityState.GAME.value
                )
                SharedPreferenceUtil.putString(requireActivity(), SharedPreferenceKey.UUID, uuid)

                val intent = Intent(requireActivity(), MainActivity::class.java).apply {
                    addFlags(FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                startActivity(intent)
                requireActivity().finish()
            }
        }

        viewModel.isButtonEnabled.observe(viewLifecycleOwner, { isEnabled ->
            binding.button.isEnabled = isEnabled
        })

        return binding.root

    }

    private fun getUUID(name: String, device: String, version: String) =
        runBlocking {
            APIRepository.registerUser(name, device, version)!!.uuid
        }
}
