package com.mwigzell.places.activities;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.mwigzell.places.R;
import com.mwigzell.places.dagger.Injection;
import com.mwigzell.places.model.Type;
import com.mwigzell.places.redux.ActionCreator;
import com.mwigzell.places.redux.AppState;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.HasSupportFragmentInjector;
import timber.log.Timber;

/**
 * Show types in a list
 */

public class TypesFragment extends BaseFragment, HasSupportFragmentInjector {
    @BindView(R.id.progressSpinner)
    ProgressBar progressSpnner;

    @BindView(R.id.myList)
    RecyclerView recyclerView;

    @Inject
    ActionCreator actionCreator;

    @Inject
    TypesViewAdapter adapter;

    public TypesFragment() {
        Injection.instance().getComponent().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Timber.d("onCreateView");

        View view = inflater.inflate(R.layout.types_fragment, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Timber.d("onActivityCreated");

        progressSpnner.setVisibility(View.VISIBLE);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        actionCreator.loadTypes();
        adapter = new TypesViewAdapter();
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onStateChanged() {
        AppState.States state = store.getState().state();
        //Timber.d("Got state=" + state);
        switch(state) {
            case TYPES_LOADED:
                progressSpnner.setVisibility(View.GONE);
                adapter.setItems(store.getState().types());
                break;
        }
    }
}
