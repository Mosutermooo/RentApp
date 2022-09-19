package com.example.rentapp.ui.fragments.settings_fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rentapp.R
import com.example.rentapp.adapters.RentsAdapter
import com.example.rentapp.databinding.FragmentUserRentsBinding
import com.example.rentapp.uitls.Resource
import com.example.rentapp.uitls.Resources
import com.example.rentapp.uitls.Resources.hideProgressDialog
import com.example.rentapp.uitls.Resources.showProgressDialog
import com.example.rentapp.uitls.Resources.showSnackBar
import com.example.rentapp.view_models.RentViewModel
import kotlinx.coroutines.flow.collect


class UserRentsFragment : Fragment() {

    private lateinit var binding: FragmentUserRentsBinding
    private lateinit var rentViewModel: RentViewModel
    private lateinit var rentAdapter: RentsAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentUserRentsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rentAdapter = RentsAdapter()

        rentViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application).create(
            RentViewModel::class.java)

        Resources.setupToolBar(
            binding.Toolbar,
            requireActivity() as AppCompatActivity,
            getString(R.string.rents)
        )

        getUserRents()
        binding.Rents.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = rentAdapter
        }


    }

    private fun getUserRents() {
        rentViewModel.getUserRents()
        lifecycleScope.launchWhenStarted {
            rentViewModel.userRentsState.collect{
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
                    is Resource.Loading -> {
                        showProgressDialog()
                    }
                    is Resource.Success -> {
                        hideProgressDialog()
                        rentAdapter.differ.submitList(it.data?.rents)
                    }
                    else -> {}
                }
            }
        }
    }

}