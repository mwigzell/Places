package com.mwigzell.places.ui.main

import androidx.recyclerview.widget.RecyclerView
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import com.bumptech.glide.Glide
import com.mwigzell.places.PlacesApplication
import com.mwigzell.places.R
import com.mwigzell.places.model.Place
import javax.inject.Inject

/**
 * Adapt List<Place> to RecyclerView.Adapter
</Place> */

class PlacesViewAdapter @Inject internal constructor() : RecyclerView.Adapter<PlacesViewAdapter.ListItemViewHolder>() {
    private val selectedItems: SparseBooleanArray
    private var items: List<Place>? = null

    init {
        selectedItems = SparseBooleanArray()
    }

    fun setItems(items: List<Place>) {
        this.items = items
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ListItemViewHolder {
        val itemView = LayoutInflater.from(viewGroup.context).inflate(R.layout.place_item, viewGroup, false)
        return ListItemViewHolder(itemView)
    }

    private fun getPhoto(place: Place, imageView: ImageView) {
        if (place.photos == null || place.photos.size == 0) {
            Glide.clear(imageView)
            return
        }
        val photo = place.photos[0]
        val photoreference = photo.photoReference
        val restaurantpic = "https://maps.googleapis.com/maps/api/place/photo?" +
                "maxwidth=400" +
                "&photoreference=" + photoreference +
                "&key=" + PlacesApplication.GOOGLE_PLACES_API_KEY

        Glide
                .with(imageView.context)
                .load(restaurantpic)
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher)
                .crossFade()
                .into(imageView)
    }

    override fun onBindViewHolder(viewHolder: ListItemViewHolder, position: Int) {
        val model = items!!.get(position)
        viewHolder.name.text = model.name.toString()
        getPhoto(model, viewHolder.imageView)
        viewHolder.itemView.isActivated = selectedItems.get(position, false)
    }

    override fun getItemCount(): Int {
        return items?.size ?: 0
    }

    class ListItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var name: TextView
        internal var imageView: ImageView

        init {
            name = itemView.findViewById<View>(R.id.name) as TextView
            imageView = itemView.findViewById<View>(R.id.imageView) as ImageView
        }
    }
}
