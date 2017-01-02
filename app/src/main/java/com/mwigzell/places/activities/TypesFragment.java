package com.mwigzell.places.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import timber.log.Timber;

/**
 * Show types in a list
 */

public class TypesFragment extends BaseFragment {
    @BindView(R.id.emptyList)
    ProgressBar emptyList;

    @BindView(R.id.myList)
    RecyclerView recyclerView;

    @Inject
    ActionCreator actionCreator;

    TypesViewAdapter adapter;

    private static List<Type> typesList;

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

        emptyList.setVisibility(View.VISIBLE);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        typesList = new ArrayList<>();
        actionCreator.loadTypes();
        adapter = new TypesViewAdapter(typesList);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onStateChanged() {
        AppState.States state = store.getState().state();
        //Timber.d("Got state=" + state);
        switch(state) {
            case TYPES_LOADED:
                emptyList.setVisibility(View.GONE);
                typesList.clear();
                typesList.addAll(store.getState().types());
                adapter.notifyDataSetChanged();
                break;
        }
    }
}
