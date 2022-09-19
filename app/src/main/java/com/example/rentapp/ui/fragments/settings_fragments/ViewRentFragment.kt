package com.example.rentapp.ui.fragments.settings_fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.example.models.AllRentsResponseParams
import com.example.rentapp.R
import com.example.rentapp.databinding.FragmentViewRentBinding


class ViewRentFragment : Fragment() {

    private val args by navArgs<ViewRentFragmentArgs>()
    private lateinit var binding: FragmentViewRentBinding
    private var rent: AllRentsResponseParams? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentViewRentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rent = args.rent
        Log.e("rent", "$rent")


    }


}