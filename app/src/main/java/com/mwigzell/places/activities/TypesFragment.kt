package com.mwigzell.places.activities

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar

import com.mwigzell.places.R
import com.mwigzell.places.dagger.Injection
import com.mwigzell.places.model.Type
import com.mwigzell.places.redux.ActionCreator
import com.mwigzell.places.redux.AppState

import java.util.ArrayList

import javax.inject.Inject

import butterknife.BindView
import butterknife.ButterKnife
import dagger.android.support.HasSupportFragmentInjector
import timber.log.Timber

/**
 * Show types in a list
 */

class TypesFragment : BaseFragment, HasSupportFragmentInjector {
    @BindView(R.id.progressSpinner)
    internal var progressSpnner: ProgressBar? = null

    @BindView(R.id.myList)
    internal var recyclerView: RecyclerView? = null

    @Inject
    internal var actionCreator: ActionCreator? = null

    @Inject
    internal var adapter: TypesViewAdapter

    init {
        Injection.instance().getComponent().inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Timber.d("onCreateView")

        val view = inflater.inflate(R.layout.types_fragment, container, false)
        ButterKnife.bind(this, view)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Timber.d("onActivityCreated")

        progressSpnner!!.visibility = View.VISIBLE
        recyclerView!!.setHasFixedSize(true)
        val llm = LinearLayoutManager(activity)
        llm.orientation = LinearLayoutManager.VERTICAL
        recyclerView!!.layoutManager = llm
        actionCreator!!.loadTypes()
        adapter = TypesViewAdapter()
        recyclerView!!.adapter = adapter
    }

    override fun onStateChanged() {
        val state = store.state.state()
        //Timber.d("Got state=" + state);
        when (state) {
            AppState.States.TYPES_LOADED -> {
                progressSpnner!!.visibility = View.GONE
                adapter.setItems(store.state.types())
            }
        }
    }
}
