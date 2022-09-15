package com.example.rentapp.adapters.view_holders

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.example.models.Car
import com.example.rentapp.R
import com.example.rentapp.databinding.CarItemViewBinding

sealed class HomeRecyclerViewHolder(binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {

    class CarViewHolder(
        private val binding: CarItemViewBinding
    ) : HomeRecyclerViewHolder(binding) {
        fun bind(car: Car){
            if(car.status == "available"){
                binding.status.text = car.status
                binding.status.setTextColor(
                    ContextCompat.getColor(binding.root.context, android.R.color.holo_green_dark)
                )
            }else{
                binding.status.text = car.status
                binding.status.setTextColor(
                    ContextCompat.getColor(binding.root.context, android.R.color.holo_red_dark)
                )
            }
            binding.brandModelType.text = "${car.car_Brand}-${car.car_Model}-${car.car_Type}"
            binding.price.text = "$${car.totalPrice}/day"
            Glide.with(binding.root)
                .load(car.carImage[0])
                .placeholder(R.color.GrayishColor)
                .centerCrop()
                .into(binding.carImage)
        }
    }



}