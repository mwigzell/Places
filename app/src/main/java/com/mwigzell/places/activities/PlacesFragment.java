package com.mwigzell.places.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.mwigzell.places.R;
import com.mwigzell.places.dagger.Injection;
import com.mwigzell.places.data.LocationService;
import com.mwigzell.places.model.Place;
import com.mwigzell.places.model.Type;
import com.mwigzell.places.network.NetworkService;
import com.mwigzell.places.redux.ActionCreator;
import com.mwigzell.places.redux.AppAction;
import com.mwigzell.places.redux.AppState;
import com.mwigzell.places.redux.original.Store;
import com.mwigzell.places.redux.original.Subscriber;
import com.mwigzell.places.redux.original.Subscription;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

import static android.R.attr.type;

/**
 *
 */

public class PlacesFragment extends BaseFragment {
    @BindView(R.id.emptyList)
    ProgressBar emptyList;

    @BindView(R.id.myList)
    RecyclerView recyclerView;

    @Inject
    ActionCreator actionCreator;

    @Inject
    NetworkService networkService;

    PlacesViewAdapter adapter;

    private static List<Place> placesList;

    public PlacesFragment() {
        Injection.instance().getComponent().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Timber.d("onCreateView");

        View view = inflater.inflate(R.layout.places_fragment, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Timber.d("onActivityCreated");

        emptyList.setVisibility(View.VISIBLE);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        placesList = new ArrayList<Place>();
        super.checkResumeLocation();
        adapter = new PlacesViewAdapter(placesList);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onStateChanged() {
        AppState.States state = store.getState().state;
        Timber.d("Got state=" + state);
        switch(state) {
            case PLACES_DOWNLOADED:
                emptyList.setVisibility(View.GONE);
                placesList.clear();
                placesList.addAll(store.getState().placeState.places);
                adapter.notifyDataSetChanged();
                break;
            case LOCATION_UPDATED:
                Location location = store.getState().location;
                String loc = location.getLatitude() + "," + location.getLongitude();
                Type selectedType = store.getState().selectedType;
                String name = selectedType == null ? "restaurant" : selectedType.name;
                networkService.getPlaces(loc, "5000", name);
                break;
        }
    }
}
