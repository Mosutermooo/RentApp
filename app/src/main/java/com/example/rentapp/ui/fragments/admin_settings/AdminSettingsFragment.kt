package com.example.rentapp.ui.fragments.admin_settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.example.rentapp.R
import com.example.rentapp.databinding.FragmentAdminSettingsBinding
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



    }

}