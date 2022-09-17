package com.example.rentapp.adapters

import android.graphics.drawable.Drawable
import android.view.*
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.graphics.drawable.toBitmap
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.models.CarImage
import com.example.rentapp.R
import com.example.rentapp.ui.activies.RentScreen
import com.example.rentapp.uitls.Resources

class CarImageViewPagerAdapter(
    private var carImages: List<CarImage>,
    private val rentScreen: RentScreen,
    private val imageVibrant: ConstraintLayout
) : RecyclerView.Adapter<CarImageViewPagerAdapter.ViewHolder>() {
    inner class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        val carImageView = view.findViewById<ImageView>(R.id.carImageView)!!
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.car_image_view_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val image = carImages[position]
        displayImage(image.imageUrl, holder)

    }

    override fun getItemCount(): Int = carImages.size

    private fun displayImage(imageUrl: String, holder: ViewHolder) {
        Glide.with(holder.itemView)
            .load(imageUrl)
            .fitCenter()
            .listener(object  : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    Resources.showSnackBar(
                        "Couldn't load image",
                        rentScreen
                    )
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    Palette.from(resource!!.toBitmap()).generate(){
                        it?.let {
                            val color = it.mutedSwatch?.rgb ?: R.color.black
                            imageVibrant.setBackgroundColor(color)
                            val window: Window = rentScreen.window
                            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                            window.statusBarColor = color
                        }
                    }
                    return false
                }

            }).into(holder.carImageView)
    }

}