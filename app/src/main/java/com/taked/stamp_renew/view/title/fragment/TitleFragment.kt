package com.taked.stamp_renew.view.title.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.taked.stamp_renew.R
import com.taked.stamp_renew.databinding.FragmentTitleBinding
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

            button.setOnClickListener {
                requireActivity().apply {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, RegisterFragment())
                        .addToBackStack(null).commit()
                }
            }
        }
        return binding.root
    }
}