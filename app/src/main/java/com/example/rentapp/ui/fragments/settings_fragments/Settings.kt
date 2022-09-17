package com.example.rentapp.ui.fragments.settings_fragments

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
import com.example.rentapp.databinding.FragmentSettingsBinding
import com.example.rentapp.ui.activies.AuthActivity
import com.example.rentapp.uitls.Resource
import com.example.rentapp.uitls.Resources
import com.example.rentapp.view_models.UserViewModel


class Settings : Fragment() {

    private lateinit var binding: FragmentSettingsBinding
    private lateinit var userViewModel: UserViewModel
    private var isEditProfile: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application).create(
            UserViewModel::class.java)
        isUserLoggedIn()

        Resources.setupToolBar(
            binding.Toolbar,
            requireActivity() as AppCompatActivity,
            getString(R.string.settings),
            "",
        )
        binding.EditProfile.setOnClickListener {
            if(!isEditProfile){
                val intent = Intent(requireActivity(), AuthActivity::class.java)
                requireActivity().startActivity(intent)
            }else{

            }
        }
    }

    private fun isUserLoggedIn() = lifecycleScope.launchWhenStarted {
        userViewModel.getUserData()
        userViewModel.getUserDataState.collect{
            when(it){
                is Resource.Error -> {
                    if(it.message == "Error Not Logged In"){
                        isEditProfile = false
                        binding.EditProfile.text = getString(R.string.login_signup)
                        binding.emailOrNotLoggedIn.text = getString(R.string.log_in_or_sign_up)
                        binding.emailOrNotLoggedIn.visibility = View.VISIBLE
                    }
                }
                is Resource.Success -> {
                    it.data?.let { user ->
                        isEditProfile = true
                        binding.EditProfile.text = getString(R.string.edit_profile)
                        binding.emailOrNotLoggedIn.text = user.email
                        binding.nameLastname.text = "${user.name} - ${user.lastname}"
                        binding.emailOrNotLoggedIn.visibility = View.VISIBLE
                        binding.nameLastname.visibility = View.VISIBLE
                    }

                }
            }
        }
    }




}