package com.example.rentapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.models.AllRentsResponseParams
import com.example.rentapp.R
import com.example.rentapp.databinding.FragmentUserRentsBinding
import com.example.rentapp.databinding.RentItemViewBinding
import java.util.concurrent.TimeUnit

class RentsAdapter : RecyclerView.Adapter<RentsAdapter.ViewHolder>() {
    inner class ViewHolder(private val binding: RentItemViewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(rent: AllRentsResponseParams) {
            binding.brandModelType.text = "${rent.car?.car_Brand}-${rent.car?.car_Model}-${rent.car?.car_Type}"
            binding.rentId.text = rent.rentId.toString()
            binding.rentPrice.text = "$${rent.price.toString()}"
            val rentTime = rent.rentTime?.div(86400000L)
            binding.renttime.text = rentTime.toString()
            val imageUrl = rent.car?.carImage?.get(0)?.imageUrl
            Glide.with(binding.root)
                .load(imageUrl)
                .placeholder(R.color.GrayishColor)
                .centerCrop()
                .into(binding.carImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            RentItemViewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val rent = differ.currentList[position]
        holder.bind(rent)
    }

    override fun getItemCount(): Int = differ.currentList.size


    private val differCallback = object : DiffUtil.ItemCallback<AllRentsResponseParams>() {
        override fun areItemsTheSame(oldItem: AllRentsResponseParams, newItem: AllRentsResponseParams): Boolean {
            return oldItem.rentId == newItem.rentId
        }

        override fun areContentsTheSame(oldItem: AllRentsResponseParams, newItem: AllRentsResponseParams): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)



}
