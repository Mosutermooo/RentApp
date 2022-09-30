package com.example.rentapp.ui.fragments.auth_fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.rentapp.R
import com.example.rentapp.databinding.FragmentLoginBinding
import com.example.rentapp.ui.activies.MainActivity
import com.example.rentapp.ui.dialogs.AddUsernameDialog
import com.example.rentapp.uitls.Resource
import com.example.rentapp.uitls.Resources
import com.example.rentapp.view_models.AuthViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var authViewModel: AuthViewModel
    private lateinit var gso: GoogleSignInOptions
    private lateinit var gsc: GoogleSignInClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()
        gsc = GoogleSignIn.getClient(requireActivity(), gso)


        authViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
            .create(AuthViewModel::class.java)
        Resources.initProgressDialog(requireContext())
        Resources.setupToolBar(
            binding.Toolbar,
            requireActivity() as AppCompatActivity,
            getString(R.string.log_in)
        )

        binding.GoogleSignIn.setOnClickListener {
            val signInIntent = gsc.signInIntent
            startActivityForResult(signInIntent, 303)
        }

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
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            startActivity(intent)
                            ActivityCompat.finishAffinity(requireActivity())
                            requireActivity().finish()
                            Resources.showSnackBar(
                                "Logged in successfully",
                                requireActivity() as AppCompatActivity
                            )

                        }
                        is Resource.Idle -> {}
                        is Resource.Internet -> {}
                    }
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK && data != null){
            when(requestCode){
                303 -> {
                    val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                    try {
                        task.result
                        val account = GoogleSignIn.getLastSignedInAccount(requireContext())
                        AddUsernameDialog(account).show(requireFragmentManager(), "ADD_USERNAME_DIALOG_TAG")
                    }catch (e: ApiException){

                    }
                }
            }
        }
    }

}