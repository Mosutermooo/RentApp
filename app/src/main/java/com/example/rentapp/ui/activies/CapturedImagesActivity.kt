package com.example.rentapp.ui.activies

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import com.example.rentapp.R
import com.example.rentapp.adapters.CapturedImagesAdapter
import com.example.rentapp.databinding.ActivityCapturedImagesBinding
import com.example.rentapp.uitls.Initializers
import com.example.rentapp.uitls.Resources
import com.example.rentapp.view_models.CarViewModel

class CapturedImagesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCapturedImagesBinding
    private lateinit var imagesAdapter: CapturedImagesAdapter
    private lateinit var carViewModel: CarViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCapturedImagesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val carId = intent.getStringExtra("car_id_captured_images")


        Resources.setupToolBar(
            binding.Toolbar,
            this,
            "Captured images"
        )

        carViewModel = Initializers().initCarViewModel(this)
        imagesAdapter = CapturedImagesAdapter()

        binding.savedImage.apply {
            layoutManager = GridLayoutManager(this@CapturedImagesActivity, 2)
            adapter = imagesAdapter
        }
        carViewModel.getAllCachedCarImagesByCarId(carId?.toInt()!!).observe(this){
            imagesAdapter.differ.submitList(it)
        }

        imagesAdapter.setOnClickListener {
            val intent = Intent(this, ShowImageActivity::class.java)
            intent.putExtra("image_uri", it.imageUri)
            startActivity(intent)
        }

    }
}