package com.example.rentapp.ui.fragments.settings_fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rentapp.R
import com.example.rentapp.adapters.RentsAdapter
import com.example.rentapp.databinding.FragmentUserRentsBinding
import com.example.rentapp.uitls.Resource
import com.example.rentapp.uitls.Resources
import com.example.rentapp.uitls.Resources.hideProgressDialog
import com.example.rentapp.uitls.Resources.initProgressDialog
import com.example.rentapp.uitls.Resources.showProgressDialog
import com.example.rentapp.uitls.Resources.showSnackBar
import com.example.rentapp.view_models.RentViewModel


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
        initProgressDialog(requireContext())
        disableNoInternetWidgets()

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

        binding.refresh.setOnRefreshListener {
            getUserRents()
        }

        rentAdapter.setOnClickListener {
            val action = UserRentsFragmentDirections.actionUserRentsFragmentToViewRentFragment(it.rentId.toString())
            findNavController().navigate(action)
        }


    }

    private fun getUserRents() {
        rentViewModel.getUserRents()
        lifecycleScope.launchWhenStarted {
            rentViewModel.userRentsState.collect{
                when(it){
                    is Resource.Error -> {
                        hideProgressDialog()
                        enableRecyclerView()
                        binding.refresh.isRefreshing = false
                        disableNoInternetWidgets()
                        it.message?.let { message ->
                            showSnackBar(
                                message,
                                requireActivity() as AppCompatActivity
                            )
                        }
                    }
                    is Resource.Loading -> {
                        binding.refresh.isRefreshing = false
                        showProgressDialog()
                    }
                    is Resource.Success -> {
                        hideProgressDialog()
                        enableRecyclerView()
                        disableNoInternetWidgets()
                        rentAdapter.differ.submitList(it.data?.rents)
                    }
                    is Resource.Internet -> {
                        binding.refresh.isRefreshing = false
                        disableRecyclerView()
                        hideProgressDialog()
                        enableNoInternetWidgets()
                        binding.noInternetText.text = it.message
                    }
                    else -> {}
                }
            }
        }
    }

    private fun enableRecyclerView(){
        binding.Rents.visibility = View.VISIBLE
    }
    private fun disableRecyclerView(){
        binding.Rents.visibility = View.VISIBLE
    }

    private fun enableNoInternetWidgets(){
        binding.noInternetText.visibility = View.VISIBLE
        binding.anim.playAnimation()
        binding.anim.visibility = View.VISIBLE
    }
    private fun disableNoInternetWidgets(){
        binding.noInternetText.visibility = View.GONE
        binding.anim.pauseAnimation()
        binding.anim.visibility = View.GONE
    }



}