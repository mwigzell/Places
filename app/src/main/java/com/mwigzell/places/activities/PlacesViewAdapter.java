package com.mwigzell.places.activities;

import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mwigzell.places.Application;
import com.mwigzell.places.R;
import com.mwigzell.places.model.Place;

import java.util.List;

/**
 *  Adapt List<Place> to RecyclerView.Adapter
 */

public class PlacesViewAdapter extends RecyclerView.Adapter<PlacesViewAdapter.ListItemViewHolder> {

    private List<Place> items;
    private SparseBooleanArray selectedItems;

    PlacesViewAdapter(List<Place> modelData) {
        if (modelData == null) {
            throw new IllegalArgumentException("modelData must not be null");
        }
        items = modelData;
        selectedItems = new SparseBooleanArray();
    }

    @Override
    public ListItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.place_item, viewGroup, false);
        return new ListItemViewHolder(itemView);
    }

    private void getPhoto(Place place, ImageView imageView) {
        if (place.photos == null || place.photos.size() == 0)
            return;
        Place.Photo photo = place.photos.get(0);
        String photoreference = photo.photoReference;
        String restaurantpic = "https://maps.googleapis.com/maps/api/place/photo?" +
                "maxwidth=400" +
                "&photoreference=" +photoreference +
                "&key=" + Application.GOOGLE_PLACES_API_KEY;

        //Timber.d("Loading restaurantpic=" + restaurantpic);

        Glide
            .with(imageView.getContext())
            .load(restaurantpic)
            .centerCrop()
            //.placeholder(R.mipmap.ic_launcher)
            .crossFade()
            .into(imageView);
    }
    @Override
    public void onBindViewHolder(ListItemViewHolder viewHolder, int position) {
        Place model = items.get(position);
        viewHolder.name.setText(String.valueOf(model.name));
        getPhoto(model, viewHolder.imageView);
        viewHolder.itemView.setActivated(selectedItems.get(position, false));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public final static class ListItemViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView imageView;

        public ListItemViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            imageView = (ImageView)itemView.findViewById(R.id.imageView);
        }
    }
}
