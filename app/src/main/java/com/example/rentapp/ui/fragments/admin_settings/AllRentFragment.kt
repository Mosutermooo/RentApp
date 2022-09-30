package com.example.rentapp.ui.fragments.admin_settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rentapp.R
import com.example.rentapp.adapters.LocationsAdapter
import com.example.rentapp.adapters.RentsAdapter
import com.example.rentapp.databinding.FragmentAllRentFramgentBinding
import com.example.rentapp.ui.fragments.settings_fragments.UserRentsFragmentDirections
import com.example.rentapp.uitls.Initializers
import com.example.rentapp.uitls.Resource
import com.example.rentapp.uitls.Resources
import com.example.rentapp.uitls.Resources.hideProgressDialog
import com.example.rentapp.uitls.Resources.initProgressDialog
import com.example.rentapp.uitls.Resources.showProgressDialog
import com.example.rentapp.uitls.Resources.showSnackBar
import com.example.rentapp.view_models.RentViewModel
import kotlinx.coroutines.flow.collect


class AllRentFragment : Fragment() {

    private lateinit var binding: FragmentAllRentFramgentBinding
    private lateinit var rentViewModel: RentViewModel
    private lateinit var rentsAdapter: RentsAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentAllRentFramgentBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initProgressDialog(requireContext())

        rentViewModel = Initializers().initRentViewModel(requireActivity() as AppCompatActivity)
        rentsAdapter = RentsAdapter()

        Resources.setupToolBar(
            binding.Toolbar,
            requireActivity() as AppCompatActivity,
            getString(R.string.rents),
        )

        binding.Rents.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = rentsAdapter
        }

        getAllRents()

        rentsAdapter.setOnClickListener {

        }




    }

    private fun getAllRents() {
        rentViewModel.getAllRents()
        lifecycleScope.launchWhenStarted {
            rentViewModel.allRentsState.collect{
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
                    is Resource.Idle -> {}
                    is Resource.Internet -> {
                        hideProgressDialog()
                        it.message?.let { message ->
                            showSnackBar(
                                message,
                                requireActivity() as AppCompatActivity
                            )
                        }
                    }
                    is Resource.Loading -> {
                        showProgressDialog()
                    }
                    is Resource.Success -> {
                        hideProgressDialog()
                        rentsAdapter.differ.submitList(it.data?.rents)
                    }
                }
            }
        }
    }


}