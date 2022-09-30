package com.example.rentapp.ui.fragments.admin_settings.add_car_fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.rentapp.R
import com.example.rentapp.adapters.CachedUploadsAdapter
import com.example.rentapp.databinding.FragmentCachedCarUploadsBinding
import com.example.rentapp.models.add_car_cache_models.CacheUploadCarModel
import com.example.rentapp.uitls.Initializers
import com.example.rentapp.uitls.Resources
import com.example.rentapp.uitls.Resources.getRandomNumberId
import com.example.rentapp.view_models.CarViewModel


class CachedCarUploads : Fragment() {

    private lateinit var binding: FragmentCachedCarUploadsBinding
    private lateinit var carViewModel: CarViewModel
    private lateinit var cachedUploadsAdapter: CachedUploadsAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentCachedCarUploadsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cachedUploadsAdapter = CachedUploadsAdapter()
        carViewModel = Initializers().initCarViewModel(requireActivity() as AppCompatActivity)
        Resources.setupToolBar(
            binding.Toolbar,
            requireActivity() as AppCompatActivity,
            getString(R.string.car_upload_chache_title),
            getString(R.string.cached_car_upload_subtitle)
        )
        binding.AddCar.setOnClickListener {
            val id = getRandomNumberId()
            val car = CacheUploadCarModel(null,
                id, "", "","",0,"","")
            carViewModel.addACarUploadCache(car)
            val action = CachedCarUploadsDirections.actionCachedCarUploadsToAddCarWizardFragment(car)
            findNavController().navigate(action)
        }



        binding.cachedCarUploads.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = cachedUploadsAdapter
        }

        carViewModel.getAllCacheCarUploads().observe(viewLifecycleOwner){
            cachedUploadsAdapter.differ.submitList(it)
        }

        cachedUploadsAdapter.setOnClickListener {
            val action = CachedCarUploadsDirections.actionCachedCarUploadsToAddCarWizardFragment(it)
            findNavController().navigate(action)
        }






    }






}