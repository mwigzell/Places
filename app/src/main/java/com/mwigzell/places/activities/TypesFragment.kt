package com.mwigzell.places.activities

import android.content.Context
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar

import com.mwigzell.places.R
import com.mwigzell.places.redux.ActionCreator
import com.mwigzell.places.redux.AppState

import javax.inject.Inject

import butterknife.BindView
import butterknife.ButterKnife
import dagger.android.support.AndroidSupportInjection
import timber.log.Timber

/**
 * Show types in a list
 */

class TypesFragment : BaseFragment() {

    @BindView(R.id.progressSpinner)
    lateinit internal var progressSpnner: ProgressBar

    @BindView(R.id.myList)
    lateinit internal var recyclerView: RecyclerView

    @Inject
    lateinit internal var actionCreator: ActionCreator

    @Inject
    lateinit internal var adapter: TypesViewAdapter

    /*@Inject lateinit var viewModelFactory: SimpleFragmentActivityViewModelFactory
    private val viewModel: SimpleFragmentActivityViewModel by lazy {
        ViewModelProviders.of(activity!!, viewModelFactory)
                .get(SimpleFragmentActivityViewModel::class.java)
    }*/

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
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

        progressSpnner.visibility = View.VISIBLE
        recyclerView.setHasFixedSize(true)
        val llm = LinearLayoutManager(activity)
        llm.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = llm
        actionCreator.loadTypes()
        recyclerView.adapter = adapter
    }

    override fun onStateChanged() {
        val state = store.state.state()
        //Timber.d("Got state=" + state);
        when (state) {
            AppState.States.TYPES_LOADED -> {
                progressSpnner.visibility = View.GONE
                adapter.setItems(store.state.types())
            }
        }
    }
}
