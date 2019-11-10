package com.mwigzell.places.activities

import androidx.recyclerview.widget.RecyclerView
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import com.bumptech.glide.Glide
import com.mwigzell.places.R
import com.mwigzell.places.model.Type
import com.mwigzell.places.redux.ActionCreator

import java.util.ArrayList

import javax.inject.Inject

import timber.log.Timber

/**
 * Adapt List<String> to RecyclerView
</String> */

class TypesViewAdapter @Inject
internal constructor(private val actionCreator: ActionCreator
) : RecyclerView.Adapter<TypesViewAdapter.ListItemViewHolder>() {
    private var items: List<Type>? = null
    private val selectedItems: SparseBooleanArray

    init {
        items = ArrayList()
        selectedItems = SparseBooleanArray()
    }

    fun setItems(items: List<Type>) {
        this.items = items
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ListItemViewHolder {
        val itemView = LayoutInflater.from(viewGroup.context).inflate(R.layout.type_item, viewGroup, false)
        return this.ListItemViewHolder(itemView)
    }

    override fun onBindViewHolder(viewHolder: ListItemViewHolder, position: Int) {
        val model = items!![position]
        viewHolder.nameView.text = model.name
        viewHolder.boundModel = model
        if (model.url != null && model.url.length > 0) {
            Glide
                    .with(viewHolder.imageView.context)
                    .load(model.url)
                    .centerCrop()
                    .crossFade()
                    .into(viewHolder.imageView)
        } else {
            viewHolder.imageView.setImageResource(0)
        }
        viewHolder.itemView.isActivated = selectedItems.get(position, false)
    }

    override fun getItemCount(): Int {
        return items?.size ?: 0
    }

    inner class ListItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        internal var nameView: TextView
        internal var imageView: ImageView
        internal var boundModel: Type? = null

        init {
            nameView = itemView.findViewById<View>(R.id.name) as TextView
            imageView = itemView.findViewById<View>(R.id.imageView) as ImageView
            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            Timber.d("onClick: " + nameView.text + " position: " + adapterPosition)
            actionCreator!!.selectType(adapterPosition)
        }
    }
}
