package com.example.rentapp.ui.fragments.settings_fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.example.models.AllRentsResponseParams
import com.example.models.RentsResponseParams
import com.example.rentapp.adapters.CarImageViewPagerAdapter
import com.example.rentapp.databinding.FragmentViewRentBinding
import com.example.rentapp.ui.activies.RentScreen
import com.example.rentapp.uitls.Initializers
import com.example.rentapp.uitls.Resource
import com.example.rentapp.uitls.Resources
import com.example.rentapp.uitls.Resources.hideProgressDialog
import com.example.rentapp.uitls.Resources.initProgressDialog
import com.example.rentapp.uitls.Resources.showProgressDialog
import com.example.rentapp.uitls.Resources.showSnackBar
import com.example.rentapp.view_models.RentViewModel
import kotlinx.coroutines.flow.collect


class ViewRentFragment : Fragment() {

    private val args by navArgs<ViewRentFragmentArgs>()
    private lateinit var binding: FragmentViewRentBinding
    private lateinit var rentViewModel: RentViewModel
    private var rentId: String? = null
    private val init = Initializers()
    private lateinit var rent: RentsResponseParams
    private lateinit var carImageAdapter: CarImageViewPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentViewRentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initProgressDialog(requireContext())
        rentViewModel = init.initRentViewModel(requireActivity() as AppCompatActivity)
        rentId = args.rentId
        Log.e("rent", "$rentId")
        getRent()

        binding.rent.setOnClickListener{
            val intent = Intent(requireActivity(), RentScreen::class.java)
            intent.putExtra("car", rent.rents?.get(0)?.car)
            startActivity(intent)
        }





    }

    private fun getRent() {
        rentViewModel.getRentsByRentId(rentId.toString())
        lifecycleScope.launchWhenStarted {
            rentViewModel.rentsByRentIdState.collect{
                when(it){
                    is Resource.Error -> {
                        hideProgressDialog()
                        it.message?.let { message ->
                            showSnackBar(
                                message,
                                requireActivity() as AppCompatActivity
                            )
                        }
                    }
                    is Resource.Internet -> {
                        hideProgressDialog()
                        showSnackBar(
                            it.message.toString(),
                            requireActivity() as AppCompatActivity
                        )
                    }
                    is Resource.Loading -> {
                        showProgressDialog()
                    }
                    is Resource.Success -> {
                        hideProgressDialog()
                        rent = it.data!!
                        displayData(rent)
                    }
                    else -> {}
                }
            }
        }
    }

    private fun displayData(rents: RentsResponseParams) {
        val rent = rents.rents?.get(0)
        rent?.let {
            binding.totalPrice.text = "$${it.price}"
            carImageAdapter = CarImageViewPagerAdapter(
                rent.car.carImage,
                requireActivity() as AppCompatActivity,
                binding.imageVibrant
            )
            binding.carImageViewpager.adapter = carImageAdapter
            binding.rentId.text = "RentId: ${it.rentId}"
            binding.rentTime.text = "Rented for: ${Resources.millisToDays(rent.rentTime)} Days"
            if(it.user.name != "" && it.user.lastname != ""){
                binding.userNameAndLastname.text = "${it.user.name} - ${it.user.lastname}"
            }else{
                binding.userNameAndLastname.text = "Username: ${it.user.username} "
            }


            if(it.car.status == "available"){
                binding.status.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.holo_green_dark))
                binding.status.text = it.car.status
            }else{
                binding.status.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.holo_red_dark))
                binding.status.text = it.car.status
            }
            Resources.setupToolBar(
                binding.Toolbar,
                requireActivity() as AppCompatActivity,
                "${it.car.car_Brand}-${it.car.car_Model}-${it.car.car_Type}"
            )
        }
    }


}