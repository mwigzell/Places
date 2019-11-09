package com.mwigzell.places.activities;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mwigzell.places.R;
import com.mwigzell.places.model.Type;
import com.mwigzell.places.redux.ActionCreator;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import timber.log.Timber;

/**
 *  Adapt List<String> to RecyclerView
 */

public class TypesViewAdapter extends RecyclerView.Adapter<TypesViewAdapter.ListItemViewHolder> {
    @Inject
    ActionCreator actionCreator;

    private List<Type> items;
    private SparseBooleanArray selectedItems;

    @Inject
    TypesViewAdapter() {
        items = new ArrayList<>();
        selectedItems = new SparseBooleanArray();
    }

    public void setItems(List<Type> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @Override
    public TypesViewAdapter.ListItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.type_item, viewGroup, false);
        return new TypesViewAdapter.ListItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TypesViewAdapter.ListItemViewHolder viewHolder, int position) {
        Type model = items.get(position);
        viewHolder.nameView.setText(model.name);
        viewHolder.boundModel = model;
        if (model.url != null && model.url.length() > 0) {
            Glide
                    .with(viewHolder.imageView.getContext())
                    .load(model.url)
                    .centerCrop()
                    .crossFade()
                    .into(viewHolder.imageView);
        } else {
            viewHolder.imageView.setImageResource(0);
        }
        viewHolder.itemView.setActivated(selectedItems.get(position, false));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public final class ListItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView nameView;
        ImageView imageView;
        Type boundModel;

        public ListItemViewHolder(View itemView) {
            super(itemView);
            nameView = (TextView) itemView.findViewById(R.id.name);
            imageView = (ImageView)itemView.findViewById(R.id.imageView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Timber.d("onClick: " + nameView.getText());
            actionCreator.selectType(boundModel);
        }
    }
}
