package com.example.rentapp.ui.activies

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.rentapp.R
import com.example.rentapp.databinding.ActivityGalleryImagesBinding
import com.example.rentapp.uitls.Resources

class GalleryImagesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGalleryImagesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGalleryImagesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Resources.setupToolBar(
            binding.Toolbar,
            this,
            "Gallery Images"
        )






    }
}