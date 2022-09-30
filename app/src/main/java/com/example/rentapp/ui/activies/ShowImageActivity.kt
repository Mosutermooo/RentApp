package com.example.rentapp.ui.activies

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import com.example.rentapp.databinding.ActivityShowImageBinding
import com.example.rentapp.uitls.Resources
import com.example.rentapp.uitls.ScaleListener

class ShowImageActivity : AppCompatActivity() {

    private lateinit var scaleGestureDetector: ScaleGestureDetector
    private lateinit var binding: ActivityShowImageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowImageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val imageUri = intent.getStringExtra("image_uri")
        val imageName = imageUri?.split("/")?.last()
        scaleGestureDetector = ScaleGestureDetector(this, ScaleListener(binding.image))

        Resources.setupToolBar(
            binding.Toolbar,
            this,
            imageName,
        )

        binding.image.setImageURI(Uri.parse(imageUri))
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        scaleGestureDetector.onTouchEvent(event)
        return super.onTouchEvent(event)
    }
}