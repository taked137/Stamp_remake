package com.taked.stamp_renew.view.title.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.taked.stamp_renew.R
import com.taked.stamp_renew.databinding.FragmentTitleBinding
import com.taked.stamp_renew.model.ActivityState
import com.taked.stamp_renew.viewmodel.title.TitleViewModel

class TitleFragment : Fragment() {

    private val viewModel = TitleViewModel()
    private lateinit var binding: FragmentTitleBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentTitleBinding.inflate(inflater, container, false).apply {
            viewmodel = viewModel
            lifecycleOwner = viewLifecycleOwner
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.button.setOnClickListener {
            val dataStore = requireActivity().getSharedPreferences("DataStore", AppCompatActivity.MODE_PRIVATE)
            val editor = dataStore.edit()
            editor.putInt("state", ActivityState.GAME.value)
            editor.apply()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.container, RegisterFragment())
                .addToBackStack(null).commit()
        }
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