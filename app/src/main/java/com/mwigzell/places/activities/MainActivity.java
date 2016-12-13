package com.mwigzell.places.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.mwigzell.places.Application;
import com.mwigzell.places.R;
import com.mwigzell.places.dagger.Injection;
import com.mwigzell.places.model.Place;
import com.mwigzell.places.redux.ActionCreator;
import com.mwigzell.places.redux.AppAction;
import com.mwigzell.places.redux.AppState;
import com.mwigzell.places.redux.original.Store;
import com.mwigzell.places.redux.original.Subscriber;
import com.mwigzell.places.redux.original.Subscription;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity implements Subscriber {
    @BindView(R.id.imageView)
    ImageView imageView;

    @Inject
    ActionCreator actionCreator;

    @Inject
    Store<AppAction, AppState> store;

    Subscription subscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Injection.instance().getComponent().inject(this);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        actionCreator.init();
        actionCreator.getPlaces();
    }

    private void getPhoto(Place place) {
        Place.Photo photo = place.photos.get(0);
        if (photo == null) {
            return;
        }
        String photoreference = photo.photoReference;
        String restaurantpic = "https://maps.googleapis.com/maps/api/place/photo?" +
                "maxwidth=400" +
                "&photoreference=" +photoreference +
                "&key=" + Application.GOOGLE_PLACES_API_KEY;

        Timber.d("Loading restaurantpic=" + restaurantpic);

        Glide
            .with(this)
            .load(restaurantpic)
            .centerCrop()
            // .placeholder(R.drawable.loading_spinner)
            //.crossFade()
            .into(imageView);
    }

    @Override
    public void onStateChanged() {
        AppState.States state = store.getState().state;
        Timber.d("Got state=" + state);
        switch(state) {
            case PLACES_DOWNLOADED:
                getPhoto(store.getState().placeState.places.get(3));
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        subscription = store.subscribe(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        subscription.unsubscribe();
    }
}
