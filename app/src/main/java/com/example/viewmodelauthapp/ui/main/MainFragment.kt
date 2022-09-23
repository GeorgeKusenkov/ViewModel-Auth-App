package com.example.viewmodelauthapp.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.example.viewmodelauthapp.databinding.FragmentMainBinding
import com.google.android.material.snackbar.Snackbar

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListener()
        launchScope()
    }

    private fun launchScope() {
        viewLifecycleOwner.lifecycleScope
            .launchWhenStarted {
                viewModel.state
                    .collect { state ->
                        when (state) {
                            State.Loading -> {
                                binding.progress.isVisible = true
                                binding.loginLayout.error = null
                                binding.passwordLayout.error = null
                                binding.button.isEnabled = false
                            }
                            State.Success -> {
                                binding.progress.isVisible = false
                                binding.loginLayout.error = null
                                binding.passwordLayout.error = null
                                binding.button.isEnabled = true
                            }
                            is State.Error -> {
                                binding.progress.isVisible = false
                                binding.loginLayout.error = state.loginError
                                binding.passwordLayout.error = state.passwordError
                            }
                        }
                    }
            }

        viewLifecycleOwner.lifecycleScope
            .launchWhenStarted {
                viewModel.error
                    .collect { message ->
                        Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT).show()
                    }
            }
    }

    private fun setOnClickListener() {
        binding.button.setOnClickListener {
            val login = binding.login.text.toString()
            val password = binding.password.text.toString()
            viewModel.onSignInClick(login, password)
        }
    }
}