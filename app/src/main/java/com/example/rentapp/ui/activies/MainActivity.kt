package com.example.rentapp.ui.activies

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
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
    private lateinit var adminSettings: MenuItem
    private var shouldShowAdminIcon: Boolean = true
    private var shouldShowRentedCarIcon: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.Toolbar)
        carViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(this.application)
            .create(CarViewModel::class.java)
        userViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(this.application)
            .create(UserViewModel::class.java)
        homeAdapter = HomeRecyclerViewAdapter()
        isUserLoggedIn()
        getEveryCar()
        Resources.initProgressDialog(this)

        binding.Toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.Settings -> {
                    val intent = Intent(this, SettingsActivity::class.java)
                    startActivity(intent)
                }
            }
            true
        }

        binding.CarRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = homeAdapter
        }


        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
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

        homeAdapter.setOnClickListener {
            val intent = Intent(this, RentScreen::class.java)
            intent.putExtra("car", it)
            startActivityForResult(intent, 200)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val logout = data?.getStringExtra("logout")
        Log.e("logout text ", "$logout")
        isUserLoggedIn()
    }

    private fun getJustEveryCar() = lifecycleScope.launchWhenStarted {
        carViewModel.getEveryCarSafeCall()
        carViewModel.everyCarState.collect{
            when(it){
                is Resource.Success -> {
                    homeAdapter.differ.submitList(it.data)
                }
                is Resource.Error -> TODO()
                else -> {

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
                        binding.Toolbar.subtitle = user.username
                        if(user.role == "admin"){
                            shouldShowAdminIcon = true
                            adminSettings.isVisible = shouldShowAdminIcon
                        }
                        if(user.role == "user"){
                            shouldShowAdminIcon = false
                            adminSettings.isVisible = shouldShowAdminIcon
                        }
                    }
                }
                else -> {}
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
                else -> {}
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
                    else -> {}
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        adminSettings = menu?.findItem(R.id.AdminSettings)!!
        adminSettings.isVisible = shouldShowAdminIcon
        return true
    }


}