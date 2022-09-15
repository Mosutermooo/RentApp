package com.example.rentapp.ui.activies

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rentapp.R
import com.example.rentapp.adapters.HomeRecyclerViewAdapter
import com.example.rentapp.databinding.ActivityMainBinding
import com.example.rentapp.uitls.Resource
import com.example.rentapp.uitls.Resources
import com.example.rentapp.uitls.Resources.showSnackBar
import com.example.rentapp.view_models.CarViewModel
import com.example.rentapp.view_models.UserViewModel
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.flow.collect

class MainActivity : AppCompatActivity() {

    private lateinit var carViewModel: CarViewModel
    private lateinit var userViewModel: UserViewModel
    private lateinit var binding: ActivityMainBinding
    private lateinit var homeAdapter: HomeRecyclerViewAdapter
    private var shouldShowAdminIcon: Boolean = true
    private var shouldShowRentedCarIcon: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =  ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.Toolbar)
        carViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(this.application).create(CarViewModel::class.java)
        userViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(this.application).create(UserViewModel::class.java)
        homeAdapter = HomeRecyclerViewAdapter()
        Resources.initProgressDialog(this)


        binding.CarRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = homeAdapter
        }
        isUserLoggedIn()
        getEveryCar()

        binding.tabLayout.addOnTabSelectedListener(object  : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when(tab?.position){
                    0 -> {
                        getJustEveryCar()
                    }
                    1 -> {
                        getCarsByType("Suv")
                    }
                    2 -> {
                        getCarsByType("Sedan")
                    }
                    3 -> {
                        getCarsByType("Convertible")
                    }
                    4 -> {
                        getCarsByType("Hatchback")
                    }
                    5 -> {
                        getCarsByType("Coupe")
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })

        binding.refresh.setOnRefreshListener {
            val tab = binding.tabLayout.getTabAt(0)
            binding.tabLayout.selectTab(tab)
            getEveryCar()
        }






    }

    private fun getJustEveryCar() = lifecycleScope.launchWhenStarted {
        carViewModel.getEveryCarSafeCall()
        carViewModel.everyCarState.collect{
            when(it){
                is Resource.Success -> {
                    homeAdapter.differ.submitList(it.data)
                }
            }
        }
    }

    private fun isUserLoggedIn() = lifecycleScope.launchWhenStarted {
        userViewModel.getUserData()
        userViewModel.getUserDataState.collect{
            when(it){
                is Resource.Error -> {
                    binding.Toolbar.title = "Log In or Sign Up"
                    binding.Toolbar.subtitle = ""
                    shouldShowAdminIcon = false
                    shouldShowRentedCarIcon = false
                }
                is Resource.Success -> {
                    it.data?.let {user->
                        binding.Toolbar.title = user.name
                        binding.Toolbar.subtitle = user.lastname
                    }
                }
            }
        }
    }



    private fun getCarsByType(type: String) = lifecycleScope.launchWhenStarted{
        carViewModel.getCarByType(type)
        carViewModel.carByTypeState.collect{
            when(it){
                is Resource.Error -> {

                }
                is Resource.Loading -> {
                }
                is Resource.Success -> {
                    homeAdapter.differ.submitList(it.data)
                }
            }
        }
    }

    private fun getEveryCar() {
        carViewModel.getEveryCar()
        lifecycleScope.launchWhenStarted {
            carViewModel.everyCarState.collect{
                when(it){
                    is Resource.Error -> {
                        Resources.hideProgressDialog()
                        if(it.message == "Error 44"){
                            showSnackBar(
                                "No Internet Connection",
                                this@MainActivity
                            )
                        }
                    }
                    is Resource.Loading -> {
                        binding.refresh.isRefreshing = false
                        Resources.showProgressDialog()
                    }
                    is Resource.Success -> {
                        Resources.hideProgressDialog()
                        homeAdapter.differ.submitList(it.data)
                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        val adminSettings = menu?.findItem(R.id.AdminSettings)
        val rentedCars = menu?.findItem(R.id.UserRents)
        adminSettings?.isVisible = shouldShowAdminIcon
        rentedCars?.isVisible = shouldShowRentedCarIcon
        return true
    }


}