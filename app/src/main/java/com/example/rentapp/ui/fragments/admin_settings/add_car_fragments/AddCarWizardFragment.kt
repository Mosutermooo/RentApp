package com.example.rentapp.ui.fragments.admin_settings.add_car_fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.navArgs
import com.example.rentapp.R
import com.example.rentapp.adapters.CarUploadViewPagerAdapter
import com.example.rentapp.databinding.FragmentAddCarWizardBinding
import com.example.rentapp.models.add_car_cache_models.CacheUploadCarModel
import com.example.rentapp.uitls.GlobalState.cachedCarUpload
import com.example.rentapp.uitls.Resources.showSnackBar


class AddCarWizardFragment : Fragment() {

    private lateinit var binding: FragmentAddCarWizardBinding
    val args by navArgs<AddCarWizardFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding =  FragmentAddCarWizardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val car = args.cachedCarUpload as CacheUploadCarModel?
        if(car != null){
            cachedCarUpload = car
        }
        Log.e("car", "$cachedCarUpload")

        val fragments = arrayListOf<Fragment>(
            CarDetailsFragment(),
            CarPriceFragment(),
            AddCarLocationFragment(),
            CarImagesFragment()
        )

        val adapter = CarUploadViewPagerAdapter(fragments, requireFragmentManager(), lifecycle)
        binding.viewPager.adapter = adapter
        binding.viewPager.isUserInputEnabled = false

//        requireActivity()
//            .onBackPressedDispatcher
//            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
//                override fun handleOnBackPressed() {
//                    requireActivity().onBackPressed()
//                }
//            }
//            )


    }


}