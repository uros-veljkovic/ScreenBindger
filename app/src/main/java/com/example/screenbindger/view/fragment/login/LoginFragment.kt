package com.example.screenbindger.view.fragment.login

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import bloder.com.blitzcore.enableWhenUsing
import com.example.screenbindger.R
import com.example.screenbindger.databinding.FragmentLoginBinding
import com.example.screenbindger.util.extensions.hide
import com.example.screenbindger.util.extensions.show
import com.example.screenbindger.util.state.State
import com.example.screenbindger.util.validator.FieldValidator
import com.example.screenbindger.view.activity.main.MainActivity
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class LoginFragment : DaggerFragment() {

    @Inject
    lateinit var viewModel: LoginViewModel

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = bind(inflater, container)
        observeFieldValidation()
        initOnClickListeners()
        return view
    }

    fun bind(inflater: LayoutInflater, container: ViewGroup?): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        return binding.root
    }

    private fun observeFieldValidation() {
        binding.apply {
            btnLogin.enableWhenUsing(FieldValidator()) {
                etUserEmail.isNotEmpty()
                etUserPassword.isNotEmpty()
            }
        }
    }

    private fun initOnClickListeners() {
        binding.apply {
            tvHere.setOnClickListener {
                findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
            }
        }
    }

    override fun onResume() {
        super.onResume()

        observeUserAuthorized()
    }

    private fun observeUserAuthorized() {
        viewModel.stateObservable.value.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                is State.Success -> {
                    binding.progressBar.hide()
                    gotoMainActivity()
                }
                is State.Error -> {
                    val message = state.exception.message ?: "Unknown error"
                    showError(message)

                    binding.progressBar.hide()
                }
                is State.Loading -> {
                    binding.progressBar.show()
                }
                is State.Unrequested -> {

                }
            }
        })
    }

    private fun showError(message: String) {
        val snackbarColor =
            ResourcesCompat.getColor(resources, R.color.design_default_color_error, null)
        Snackbar.make(
            requireView(),
            message,
            Snackbar.LENGTH_LONG
        ).setBackgroundTint(snackbarColor)
            .show()

    }

    fun gotoMainActivity() {
        animateButton()
        Handler(Looper.myLooper()!!).postDelayed({
            binding.progressBar.hide()

            startActivity(Intent(requireActivity(), MainActivity::class.java))
            requireActivity().finish()
        }, 1500)
    }

    private fun animateButton() {
        binding.btnLogin.also {
            val animation = AnimationUtils.loadAnimation(requireContext(), R.anim.fadeout)
            it.startAnimation(animation)
            it.animation.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation) {
                    // TODO Auto-generated method stub
                }

                override fun onAnimationRepeat(animation: Animation) {
                    // TODO Auto-generated method stub
                }

                override fun onAnimationEnd(animation: Animation) {
                    it.visibility = View.INVISIBLE
                }
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

}