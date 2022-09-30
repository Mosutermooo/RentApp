package com.example.rentapp.uitls

import android.view.ScaleGestureDetector
import android.widget.ImageView

class ScaleListener(private val imageView: ImageView) : ScaleGestureDetector.SimpleOnScaleGestureListener() {
    private var FACTOR = 1.0f
    override fun onScale(detector: ScaleGestureDetector?): Boolean {
        detector?.let {
            FACTOR = (FACTOR * it.scaleFactor)
            FACTOR = Math.max(0.1f, Math.min(FACTOR, 10.0f))
            imageView.scaleX = FACTOR
            imageView.scaleY = FACTOR
        }
        return true
    }
}