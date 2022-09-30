package com.example.rentapp.ui.fragments.admin_settings.add_car_fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.rentapp.R
import com.example.rentapp.databinding.FragmentCarPriceBinding
import com.example.rentapp.uitls.GlobalState
import com.example.rentapp.uitls.Initializers
import com.example.rentapp.uitls.Resources.showSnackBar
import com.example.rentapp.view_models.CarViewModel


class CarPriceFragment : Fragment() {

    private lateinit var binding: FragmentCarPriceBinding
    private lateinit var carViewModel: CarViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentCarPriceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val car = GlobalState.cachedCarUpload
        val viewPager = activity?.findViewById<ViewPager2>(R.id.viewPager)
        carViewModel = Initializers().initCarViewModel(requireActivity() as AppCompatActivity)
        if (car != null){
            binding.carPrice.setText(car.car_Price.toString())
        }
        binding.back.setOnClickListener {
            viewPager?.currentItem = 0
        }
        binding.next.setOnClickListener {
            if(binding.carPrice.text.toString().isEmpty()){
                showSnackBar("Please add a car price", requireActivity() as AppCompatActivity)
                return@setOnClickListener
            }
            carViewModel.changeCachedUploadCarPrice(car?.id.toString(), binding.carPrice.text.toString())
            viewPager?.currentItem = 2
        }



    }

}