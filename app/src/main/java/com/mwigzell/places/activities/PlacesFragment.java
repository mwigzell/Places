package com.mwigzell.places.activities;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mwigzell.places.R;
import com.mwigzell.places.dagger.Injection;
import com.mwigzell.places.model.Place;
import com.mwigzell.places.model.Type;
import com.mwigzell.places.network.NetworkService;
import com.mwigzell.places.redux.ActionCreator;
import com.mwigzell.places.redux.AppState;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 *
 */

public class PlacesFragment extends BaseFragment {
    public static final String DEFAULT_LOCATION = "-33.8670522,151.1957362";

    @BindView(R.id.noResults)
    TextView noResults;

    @BindView(R.id.progressSpinner)
    ProgressBar progressSpinner;

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

        noResults.setVisibility(View.GONE);
        progressSpinner.setVisibility(View.VISIBLE);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        placesList = new ArrayList<>();
        super.checkResumeLocation();
        adapter = new PlacesViewAdapter(placesList);
        recyclerView.setAdapter(adapter);
        fetchPlaces();
    }

    private void fetchPlaces() {
        Location location = store.getState().location();
        String loc;
        if (location.getLongitude() != 0 && location.getLatitude() != 0) {
            loc = location.getLatitude() + "," + location.getLongitude();
        } else {
            loc = DEFAULT_LOCATION;
        }
        Type selectedType = store.getState().selectedType();
        String name = selectedType == null ? "restaurant" : selectedType.name;
        networkService.getPlaces(loc, "5000", name);
    }

    private void updateView() {
        if (placesList.size() > 0) {
            noResults.setVisibility(View.GONE);
            adapter.notifyDataSetChanged();
        } else {
            noResults.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateView();
    }

    @Override
    public void onStateChanged() {
        AppState.States state = store.getState().state();
        //Timber.d("Got state=" + state);
        switch(state) {
            case PLACES_DOWNLOADED:
                progressSpinner.setVisibility(View.GONE);
                placesList.clear();
                placesList.addAll(store.getState().placeState().places);
                updateView();
                break;
            case GET_PLACES_FAILED:
                noResults.setVisibility(View.VISIBLE);
                break;
            case LOCATION_UPDATED:
                break;
        }
    }
}
