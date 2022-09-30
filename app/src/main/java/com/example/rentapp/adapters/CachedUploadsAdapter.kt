package com.example.rentapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.rentapp.R
import com.example.rentapp.databinding.GridRecyclerViewImageLayoutBinding
import com.example.rentapp.models.add_car_cache_models.CacheUploadCarModel
import com.example.rentapp.models.locations_model.LocationModel

class CachedUploadsAdapter: RecyclerView.Adapter<CachedUploadsAdapter.ViewHolder>() {
    class ViewHolder(private val binding: GridRecyclerViewImageLayoutBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(car: CacheUploadCarModel){
            binding.image.setImageDrawable(ContextCompat.getDrawable(itemView.context, R.drawable.car_image))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            GridRecyclerViewImageLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val car = differ.currentList[position]
        holder.bind(car)
        holder.itemView.setOnClickListener {
            onClickListener?.invoke(car)
        }
    }

    override fun getItemCount(): Int = differ.currentList.size

        private val differCallback = object : DiffUtil.ItemCallback<CacheUploadCarModel>() {
        override fun areItemsTheSame(oldItem: CacheUploadCarModel, newItem: CacheUploadCarModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CacheUploadCarModel, newItem: CacheUploadCarModel): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)


    private var onClickListener: ((CacheUploadCarModel) -> Unit)? = null


    fun setOnClickListener(listener: (CacheUploadCarModel) -> Unit){
        onClickListener = listener
    }

}