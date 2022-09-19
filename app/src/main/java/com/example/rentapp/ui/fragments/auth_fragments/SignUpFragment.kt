package com.example.rentapp.ui.fragments.auth_fragments

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.rentapp.R
import com.example.rentapp.databinding.FragmentSignUpBinding
import com.example.rentapp.uitls.Resource
import com.example.rentapp.uitls.Resources
import com.example.rentapp.view_models.AuthViewModel


class SignUpFragment : Fragment() {

    private lateinit var binding: FragmentSignUpBinding
    private lateinit var authViewModel: AuthViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Resources.setupToolBar(
            binding.Toolbar,
            requireActivity() as AppCompatActivity,
            getString(R.string.sign_up)
        )
        Resources.initProgressDialog(requireContext())

        authViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application).create(AuthViewModel::class.java)

        binding.SignUp.setOnClickListener {
            authViewModel.signUpUser(
                binding.email.text.toString(),
                binding.Username.text.toString(),
                binding.Password.text.toString()
            )
            lifecycleScope.launchWhenStarted {
                authViewModel.signUpUserState.collect{
                    when(it){
                        is Resource.Error -> {
                            Resources.hideProgressDialog()
                            it.message?.let { message ->
                                Resources.showSnackBar(
                                    message,
                                    requireActivity() as AppCompatActivity
                                )
                            }
                        }
                        is Resource.Loading -> Resources.showProgressDialog()
                        is Resource.Success -> {
                            Resources.hideProgressDialog()
                            Resources.showSnackBar(
                                getString(R.string.signed_in_sucessfully),
                                requireActivity() as AppCompatActivity
                            )
                            Handler().postDelayed({ requireActivity().onBackPressed() }, 500)
                        }
                        is Resource.Idle -> {}
                        is Resource.Internet -> {}
                    }
                }
            }
        }

    }

}