package com.example.rentapp.adapters.view_holders

import android.opengl.Visibility
import android.view.View
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.models.Car
import com.example.rentapp.databinding.CarItemViewBinding
import com.example.rentapp.databinding.FastSelectItemViewBinding
import com.example.rentapp.models.car_models.FastSelectPlans

sealed class FastSelectViewHolder (binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {

    class FastSelectBinding(
        private val binding: FastSelectItemViewBinding
    ) : FastSelectViewHolder(binding) {
        fun bind(param : FastSelectPlans){
            binding.price.text = "$${param.price} total price"
            binding.name.text = param.name
        }
        fun setCheckVisibility(visibility: Int){
            binding.check.visibility = visibility
        }
        fun setCardViewColor(color: Int){
            binding.cardView.setCardBackgroundColor(color)
        }
    }



}