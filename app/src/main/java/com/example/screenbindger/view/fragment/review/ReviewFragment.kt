package com.example.screenbindger.view.fragment.review

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.screenbindger.R
import com.example.screenbindger.databinding.FragmentRegisterBinding
import javax.inject.Inject

class ReviewFragment : Fragment() {

    @Inject
    lateinit var viewModel: ReviewFragmentViewModel

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = bind(inflater, container)

        return view
    }

    private fun bind(inflater: LayoutInflater, container: ViewGroup?): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

}