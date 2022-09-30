package com.example.rentapp.ui.fragments.admin_settings.add_car_fragments

import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.rentapp.R
import com.example.rentapp.databinding.FragmentCarDetalisFragmentBinding
import com.example.rentapp.uitls.GlobalState
import com.example.rentapp.uitls.Initializers
import com.example.rentapp.uitls.Resources
import com.example.rentapp.uitls.Resources.showSnackBar
import com.example.rentapp.view_models.CarViewModel


class CarDetailsFragment : Fragment() {

    private lateinit var binding: FragmentCarDetalisFragmentBinding
    private lateinit var carViewModel: CarViewModel
    private var carId: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentCarDetalisFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val car = GlobalState.cachedCarUpload
        carViewModel = Initializers().initCarViewModel(requireActivity() as AppCompatActivity)

        if(car != null){
            binding.carBrand.setText(car.carBrand)
            binding.carModel.setText(car.carModel)
            binding.carType.setText(car.carType)
            carId = car.id
        }


        val viewPager = activity?.findViewById<ViewPager2>(R.id.viewPager)

        binding.back.setOnClickListener {
            requireActivity().onBackPressed()
        }
        binding.next.setOnClickListener {
            if(binding.carBrand.text.toString() == ""){
                showSnackBar("Please add the car brand", requireActivity() as AppCompatActivity)
                return@setOnClickListener
            }
            if(binding.carModel.text.toString() == ""){
                showSnackBar("Please add the car model", requireActivity() as AppCompatActivity)
                return@setOnClickListener
            }
            if(binding.carType.text.toString() == ""){
                showSnackBar("Please add the car type", requireActivity() as AppCompatActivity)
                return@setOnClickListener
            }
            carViewModel.changeCachedUploadCarDetails(
                car?.id.toString(),
                binding.carBrand.text.toString(),
                binding.carModel.text.toString(),
                binding.carType.text.toString()
            )
            viewPager?.currentItem = 1
        }





    }


}