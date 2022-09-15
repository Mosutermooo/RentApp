package com.example.rentapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.models.Car
import com.example.models.CarResponse
import com.example.rentapp.adapters.view_holders.HomeRecyclerViewHolder
import com.example.rentapp.databinding.CarItemViewBinding

class HomeRecyclerViewAdapter : RecyclerView.Adapter<HomeRecyclerViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeRecyclerViewHolder {
        return HomeRecyclerViewHolder.CarViewHolder(
            CarItemViewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: HomeRecyclerViewHolder, position: Int) {
        val car = differ.currentList[position]
        if(holder is HomeRecyclerViewHolder.CarViewHolder){
            holder.bind(car)
        }
    }

    override fun getItemCount(): Int  = differ.currentList.size

    private val differCallback = object : DiffUtil.ItemCallback<Car>() {
        override fun areItemsTheSame(oldItem: Car, newItem: Car): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Car, newItem: Car): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)
}