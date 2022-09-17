package com.example.rentapp.ui.activies

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.models.Car
import com.example.rentapp.R
import com.example.rentapp.adapters.CarImageViewPagerAdapter
import com.example.rentapp.adapters.FastSelectAdapter
import com.example.rentapp.databinding.ActivityRentScreenBinding
import com.example.rentapp.uitls.Resources.showSnackBar
import com.example.rentapp.use_cases.FastSelectUseCase
import com.example.rentapp.view_models.CarViewModel


class RentScreen : AppCompatActivity() {
    private lateinit var binding: ActivityRentScreenBinding
    private lateinit var fastSelectAdapter: FastSelectAdapter
    private var fastSelectPlansUseCase = FastSelectUseCase()
    private lateinit var addToFavorite: MenuItem
    private lateinit var carViewModel: CarViewModel
    private lateinit var addedToFavorite: MenuItem
    private var price: Int? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRentScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        carViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(this.application).create(
            CarViewModel::class.java)
        fastSelectAdapter = FastSelectAdapter()

        val car = intent.getSerializableExtra("car") as Car
        val fastSelectPrices =  fastSelectPlansUseCase.calculatePricePerDays(car.totalPrice)


        setSupportActionBar(binding.Toolbar)
        supportActionBar?.title = "${car.car_Brand}-${car.car_Model}-${car.car_Type}"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)

        binding.Toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        fastSelectAdapter.differ.submitList(fastSelectPrices)

        displayData(car)

        fastSelectAdapter.setOnClickListener {
            price = it.price
            Log.e("price", "${price}")
        }

        binding.rent.setOnClickListener {
            rentCar()
        }




    }

    private fun rentCar() {
        if(price != null){
            showSnackBar(
                "price: $price",
                this
            )
        }else{
            showSnackBar(
                "price: $price",
                this
            )
        }
    }

    fun displayData(car: Car){
        binding.plans.apply {
            layoutManager = LinearLayoutManager(this@RentScreen, LinearLayoutManager.HORIZONTAL, false)
            adapter = fastSelectAdapter
        }

        binding.totalPrice.text = "$${car.totalPrice}/per day"

        if(car.status == "available"){
            binding.status.setTextColor(ContextCompat.getColor(this, android.R.color.holo_green_dark))
            binding.status.text = car.status
        }else{
            binding.status.setTextColor(ContextCompat.getColor(this, android.R.color.holo_red_dark))
            binding.status.text = car.status
        }
        val adapter = CarImageViewPagerAdapter(car.carImage, this, binding.imageVibrant)
        binding.carImageViewpager.adapter = adapter

    }



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.rent_car_menu, menu)
        addToFavorite = menu?.findItem(R.id.addToFavorite)!!
        addedToFavorite = menu.findItem(R.id.addedToFavorite)!!
        addedToFavorite.isVisible = false
        return true
    }
}