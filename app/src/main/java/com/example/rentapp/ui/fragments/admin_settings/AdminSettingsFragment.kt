package com.example.rentapp.ui.fragments.admin_settings

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.example.rentapp.R
import com.example.rentapp.databinding.FragmentAdminSettingsBinding
import com.example.rentapp.ui.activies.AddCarActivity
import com.example.rentapp.uitls.Resources


class AdminSettingsFragment : Fragment() {

    private lateinit var binding: FragmentAdminSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentAdminSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Resources.setupToolBar(
            binding.Toolbar,
            requireActivity() as AppCompatActivity,
            getString(R.string.admin_settings)
        )

        binding.SeeLocations.setOnClickListener {
            findNavController().navigate(R.id.action_adminSettingsFragment_to_seeAllLocations)
        }

        binding.AddLocations.setOnClickListener {
            findNavController().navigate(R.id.action_adminSettingsFragment_to_addLocationFragment)
        }

        binding.DeleteLocation.setOnClickListener {
            findNavController().navigate(R.id.action_adminSettingsFragment_to_deleteLocations)
        }

        binding.ViewAllRents.setOnClickListener {
            findNavController().navigate(R.id.action_adminSettingsFragment_to_allRentFragment)
        }

        binding.AddNewCar.setOnClickListener {
            val intent = Intent(requireActivity(), AddCarActivity::class.java)
            startActivity(intent)
        }




    }

}