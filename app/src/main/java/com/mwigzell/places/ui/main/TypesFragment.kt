package com.mwigzell.places.ui.main

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.mwigzell.places.R
import com.mwigzell.places.dagger.ViewModelFactory
import com.mwigzell.places.model.Type
import com.mwigzell.places.ui.BaseFragment
import timber.log.Timber
import javax.inject.Inject

//TODO: persist last types list

/**
 * Show types in a list
 */

class TypesFragment : BaseFragment() {

    @BindView(R.id.progressSpinner)
    lateinit internal var progressSpnner: ProgressBar

    @BindView(R.id.typeList)
    lateinit internal var recyclerView: RecyclerView

    @Inject
    lateinit internal var adapter: TypesViewAdapter

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var mainViewModel: MainViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Timber.d("onCreateView")

        val view = inflater.inflate(R.layout.types_fragment, container, false)
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

        progressSpnner.visibility = View.VISIBLE
        recyclerView.setHasFixedSize(true)
        val llm = LinearLayoutManager(activity)
        llm.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = llm
        //actionCreator.loadTypes()
        recyclerView.adapter = adapter

        observeViews()
    }

    private fun observeViews() {
        //Note: rely on Support Library 28.0.0 and AndroidX 1.0.0 to destroy observers
        // every time fragment view is destroyed
        mainViewModel.getTypes().observe(this.viewLifecycleOwner, Observer<List<Type>> {
            progressSpnner.visibility = View.GONE
            adapter.setItems(it)
        })

        addDisposable(adapter.clickEvent.subscribe {
            Timber.d("clicked on $it")
            mainViewModel.onTypeSelected(it)
        })
    }
}
