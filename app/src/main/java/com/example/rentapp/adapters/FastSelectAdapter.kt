package com.example.rentapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.models.Car
import com.example.rentapp.R
import com.example.rentapp.adapters.view_holders.FastSelectViewHolder
import com.example.rentapp.databinding.FastSelectItemViewBinding
import com.example.rentapp.models.car_models.FastSelectPlans

class FastSelectAdapter : RecyclerView.Adapter<FastSelectViewHolder>() {


    var selectedItem = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FastSelectViewHolder {
        return FastSelectViewHolder.FastSelectBinding(
            FastSelectItemViewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: FastSelectViewHolder, position: Int) {
        val plan = differ.currentList[position]
        if(holder is FastSelectViewHolder.FastSelectBinding){
            holder.bind(plan)

            if(selectedItem == position){
                holder.setCardViewColor(ContextCompat.getColor(holder.itemView.context, R.color.BlueColor))
                holder.setCheckVisibility(View.VISIBLE)
            }else{
                holder.setCheckVisibility(View.GONE)
                holder.setCardViewColor(ContextCompat.getColor(holder.itemView.context, R.color.white))
            }

            holder.itemView.setOnClickListener {
                if(holder.adapterPosition == RecyclerView.NO_POSITION) return@setOnClickListener
                notifyItemChanged(selectedItem)
                selectedItem = holder.adapterPosition
                notifyItemChanged(selectedItem)
                onClickListener?.invoke(plan)
            }
        }
    }

    override fun getItemCount(): Int = differ.currentList.size

    private val differCallback = object : DiffUtil.ItemCallback<FastSelectPlans>() {
        override fun areItemsTheSame(oldItem: FastSelectPlans, newItem: FastSelectPlans): Boolean {
            return oldItem.price == newItem.price
        }

        override fun areContentsTheSame(oldItem: FastSelectPlans, newItem: FastSelectPlans): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    private var onClickListener: ((FastSelectPlans) -> Unit)? = null

    fun setOnClickListener(listener: (FastSelectPlans) -> Unit){
        onClickListener = listener
    }





}