package com.mwigzell.places.activities

import android.content.Context
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView

import com.mwigzell.places.R
import com.mwigzell.places.model.Place
import com.mwigzell.places.network.NetworkService
import com.mwigzell.places.redux.ActionCreator
import com.mwigzell.places.redux.AppState

import java.util.ArrayList

import javax.inject.Inject

import butterknife.BindView
import butterknife.ButterKnife
import dagger.android.support.AndroidSupportInjection
import timber.log.Timber

/**
 *
 */

class PlacesFragment : BaseFragment() {

    @BindView(R.id.noResults)
    lateinit internal var noResults: TextView

    @BindView(R.id.progressSpinner)
    lateinit internal var progressSpinner: ProgressBar

    @BindView(R.id.myList)
    lateinit internal var recyclerView: RecyclerView

    @Inject
    lateinit internal var actionCreator: ActionCreator

    @Inject
    lateinit internal var networkService: NetworkService

    @Inject
    lateinit internal var adapter: PlacesViewAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Timber.d("onCreateView")

        val view = inflater.inflate(R.layout.places_fragment, container, false)
        ButterKnife.bind(this, view)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Timber.d("onActivityCreated")

        noResults!!.visibility = View.GONE
        progressSpinner!!.visibility = View.VISIBLE
        recyclerView!!.setHasFixedSize(true)
        val llm = LinearLayoutManager(activity)
        llm.orientation = LinearLayoutManager.VERTICAL
        recyclerView!!.layoutManager = llm
        super.checkResumeLocation()
        recyclerView!!.adapter = adapter
        fetchPlaces()
    }

    private fun fetchPlaces() {
        val location = store.state.location()
        val loc: String
        if (location.longitude != 0.0 && location.latitude != 0.0) {
            loc = location.latitude.toString() + "," + location.longitude
        } else {
            loc = DEFAULT_LOCATION
        }
        var i = store.state.selectedPosition()
        var name = "restaurant"
        if (i < store.state.types().size)
            name = store.state.types().get(i).name
        networkService!!.getPlaces(loc, "5000", name)
    }

    private fun updateView() {
        if (adapter.itemCount > 0) {
            noResults!!.visibility = View.GONE
        } else {
            noResults!!.visibility = View.VISIBLE
        }
    }

    override fun onResume() {
        super.onResume()
        updateView()
    }

    override fun onStateChanged() {
        val state = store.state.state()
        //Timber.d("Got state=" + state);
        when (state) {
            AppState.States.PLACES_DOWNLOADED -> {
                progressSpinner!!.visibility = View.GONE
                adapter.setItems(store.state.placeState().places)
                updateView()
            }
            AppState.States.GET_PLACES_FAILED -> noResults!!.visibility = View.VISIBLE
            AppState.States.LOCATION_UPDATED -> {
            }
        }
    }

    companion object {
        val DEFAULT_LOCATION = "-33.8670522,151.1957362"
    }
}
