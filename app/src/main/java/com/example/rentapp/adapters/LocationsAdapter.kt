package com.example.rentapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.models.AllRentsResponseParams
import com.example.rentapp.databinding.FragmentSeeAllLocationsBinding
import com.example.rentapp.databinding.LocationItemViewBinding
import com.example.rentapp.models.locations_model.LocationModel

class LocationsAdapter : RecyclerView.Adapter<LocationsAdapter.ViewHolder>() {
    inner class ViewHolder(private var binding: LocationItemViewBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(location: LocationModel){
            binding.LocationName.text = location.location_name
            binding.LocationCity.text = location.city
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LocationItemViewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val location = differ.currentList[position]
        holder.bind(location)
        holder.itemView.setOnClickListener {
            onClickListener?.invoke(location)
        }
    }

    override fun getItemCount(): Int = differ.currentList.size


    private val differCallback = object : DiffUtil.ItemCallback<LocationModel>() {
        override fun areItemsTheSame(oldItem: LocationModel, newItem: LocationModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: LocationModel, newItem: LocationModel): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)


    private var onClickListener: ((LocationModel) -> Unit)? = null


    fun setOnClickListener(listener: (LocationModel) -> Unit){
        onClickListener = listener
    }






}