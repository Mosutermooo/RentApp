package com.example.rentapp.ui.fragments.auth_fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.example.rentapp.R
import com.example.rentapp.databinding.FragmentOptionBinding
import com.example.rentapp.uitls.Resources


class OptionFragment : Fragment() {

    private lateinit var binding: FragmentOptionBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOptionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.anim.playAnimation()
        Resources.setupToolBar(
            binding.Toolbar,
            requireActivity() as AppCompatActivity,
            getString(R.string.log_in_or_sign_up),
            "",
        )

        binding.Login.setOnClickListener {
            findNavController().navigate(R.id.action_optionFragment_to_loginFragment)
        }

        binding.SignUp.setOnClickListener {
            findNavController().navigate(R.id.action_optionFragment_to_signUpFragment)
        }



    }

}