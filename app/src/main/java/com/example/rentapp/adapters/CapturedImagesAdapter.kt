package com.example.rentapp.adapters

import android.graphics.BitmapFactory
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.models.Car
import com.example.rentapp.R
import com.example.rentapp.databinding.GridRecyclerViewImageLayoutBinding
import com.example.rentapp.models.add_car_cache_models.CachedCarImages
import com.example.rentapp.models.car_models.FastSelectPlans

class CapturedImagesAdapter() : RecyclerView.Adapter<CapturedImagesAdapter.ViewHolder>() {
    class ViewHolder(private val binding: GridRecyclerViewImageLayoutBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(image: CachedCarImages){
            Glide.with(itemView.context)
                .load(Uri.parse(image.imageUri))
                .placeholder(R.color.GrayishColor)
                .into(binding.image)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            GridRecyclerViewImageLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val image = differ.currentList[position]
        holder.bind(image)
        holder.itemView.setOnClickListener {
            onClickListener?.invoke(image)
        }
    }

    override fun getItemCount(): Int = differ.currentList.size

    private val differCallback = object : DiffUtil.ItemCallback<CachedCarImages>() {
        override fun areItemsTheSame(oldItem: CachedCarImages, newItem: CachedCarImages): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CachedCarImages, newItem: CachedCarImages): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    private var onClickListener: ((CachedCarImages) -> Unit)? = null

    fun setOnClickListener(listener: (CachedCarImages) -> Unit){
        onClickListener = listener
    }


}
