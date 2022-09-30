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
import androidx.navigation.fragment.findNavController
import com.example.rentapp.R
import com.example.rentapp.databinding.FragmentSettingsBinding
import com.example.rentapp.ui.activies.AuthActivity
import com.example.rentapp.ui.dialogs.LogOutDialog
import com.example.rentapp.uitls.Const
import com.example.rentapp.uitls.Resource
import com.example.rentapp.uitls.Resources
import com.example.rentapp.uitls.Resources.showSnackBar
import com.example.rentapp.uitls.UserDataStore
import com.example.rentapp.view_models.UserViewModel
import kotlinx.coroutines.launch


class Settings : Fragment() {

    private lateinit var binding: FragmentSettingsBinding
    private lateinit var userViewModel: UserViewModel
    private var isEditProfile: Boolean = false

    private lateinit var dataStore: UserDataStore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataStore = UserDataStore(requireContext())

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

        binding.LocateUs.setOnClickListener {
            findNavController().navigate(R.id.action_settings_to_seeAllLocations2)
        }

        binding.Rents.setOnClickListener{
            findNavController().navigate(R.id.action_settings_to_userRentsFragment)
        }

        binding.LogOut.setOnClickListener {
            lifecycleScope.launch{
                val userId = dataStore.read(Const.userId)
                if(userId != null){
                    LogOutDialog().show(parentFragmentManager, "LOG_OUT_DIALOG")
                }else{
                    showSnackBar(
                        getString(R.string.log_in_first),
                        requireActivity() as AppCompatActivity
                    )
                }
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
                        if(user.name == "" && user.lastname == ""){
                            binding.nameLastname.text = "No name - No lastname"
                        }else{
                            binding.nameLastname.text = "${user.name} - ${user.lastname}"
                        }
                        binding.emailOrNotLoggedIn.visibility = View.VISIBLE
                        binding.nameLastname.visibility = View.VISIBLE
                    }

                }
                is Resource.Idle -> {}
                is Resource.Loading -> {}
                is Resource.Internet -> {}
            }
        }
    }




}