package com.mwigzell.places.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.mwigzell.places.dagger.ViewModelFactory
import com.mwigzell.places.model.Place
import timber.log.Timber
import javax.inject.Inject

//TODO: persist last get places response

class PlacesFragment : BaseFragment() {

    @BindView(com.mwigzell.places.R.id.noResults)
    lateinit internal var noResults: TextView

    @BindView(com.mwigzell.places.R.id.progressSpinner)
    lateinit internal var progressSpinner: ProgressBar

    @BindView(com.mwigzell.places.R.id.myList)
    lateinit internal var recyclerView: RecyclerView

    @Inject
    lateinit internal var adapter: PlacesViewAdapter

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var mainViewModel: MainViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Timber.d("onCreateView")

        val view = inflater.inflate(com.mwigzell.places.R.layout.places_fragment, container, false)
        ButterKnife.bind(this, view)
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainViewModel = ViewModelProviders.of(activity!!, viewModelFactory)[MainViewModel::class.java]
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Timber.d("onActivityCreated")

        noResults.visibility = View.GONE
        progressSpinner.visibility = View.VISIBLE
        recyclerView.setHasFixedSize(true)
        val llm = LinearLayoutManager(activity)
        llm.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = llm
        super.checkResumeLocation()
        recyclerView.adapter = adapter

        observeViews()
    }

    private fun observeViews() {
        //Note: rely on Support Library 28.0.0 and AndroidX 1.0.0 to destroy observers
        // every time fragment view is destroyed
        mainViewModel.getPlaces().observe(this.viewLifecycleOwner, Observer<List<Place>> {
            progressSpinner.visibility = View.GONE
            adapter.setItems(it)
            updateView()
        })
    }

    private fun updateView() {
        if (adapter.itemCount > 0) {
            noResults.visibility = View.GONE
        } else {
            noResults.visibility = View.VISIBLE
        }
    }
}
