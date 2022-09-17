package com.example.rentapp.ui.fragments.auth_fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.rentapp.R
import com.example.rentapp.databinding.FragmentLoginBinding
import com.example.rentapp.ui.activies.MainActivity
import com.example.rentapp.uitls.Resource
import com.example.rentapp.uitls.Resources
import com.example.rentapp.view_models.AuthViewModel

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var authViewModel: AuthViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        authViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
            .create(AuthViewModel::class.java)
        Resources.initProgressDialog(requireContext())
        Resources.setupToolBar(
            binding.Toolbar,
            requireActivity() as AppCompatActivity,
            getString(R.string.log_in)
        )

        binding.Login.setOnClickListener {
            lifecycleScope.launchWhenStarted {
                authViewModel.loginUser(
                    binding.username.text.toString(),
                    binding.password.text.toString()
                )
                authViewModel.loginUserState.collect{
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
                            val intent = Intent(requireActivity(), MainActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                            startActivity(intent)
                            requireActivity().finish()
                            Resources.showSnackBar(
                                "Logged in successfully",
                                requireActivity() as AppCompatActivity
                            )

                        }
                        is Resource.Idle -> {}
                    }
                }
            }
        }





    }

}