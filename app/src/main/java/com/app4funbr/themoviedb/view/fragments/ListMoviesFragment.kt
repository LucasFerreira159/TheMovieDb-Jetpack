package com.app4funbr.themoviedb.view.fragments

import android.app.Application
import android.os.Bundle
import android.view.*
import android.view.animation.AnimationUtils
import android.widget.AbsListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app4funbr.themoviedb.R
import com.app4funbr.themoviedb.infrastructure.network.ServiceAPI
import com.app4funbr.themoviedb.infrastructure.util.Utils
import com.app4funbr.themoviedb.repository.NetworkState
import com.app4funbr.themoviedb.view.activity.MoviePageListRepository
import com.app4funbr.themoviedb.view.adapter.MoviesAdapter
import com.app4funbr.themoviedb.viewmodel.ListMoviesViewModel
import kotlinx.android.synthetic.main.fragment_list_movies.*

class ListMoviesFragment : Fragment() {

    private lateinit var viewModel: ListMoviesViewModel
    private lateinit var moviesRepository: MoviePageListRepository
    private lateinit var serviceAPI: ServiceAPI
    private val moviesAdapter = MoviesAdapter(arrayListOf())
    private lateinit var manager: GridLayoutManager
    private var isScrolling = false

    private var page = 1
    private var currentItems: Int = 0
    private var totalItems: Int = 0
    private var scrollOutItems = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_list_movies, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        serviceAPI = ServiceAPI()
        moviesRepository = MoviePageListRepository(serviceAPI)
        viewModel = getViewModel()
        //viewModel.refresh()

        val resId = R.anim.grid_layout_animation_from_bottom
        val animation = AnimationUtils.loadLayoutAnimation(requireContext(), resId)

        recycler_movies?.apply {
            layoutAnimation = animation
            manager = GridLayoutManager(requireContext(), 3)
            manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    val viewType = moviesAdapter.getItemViewType(position)
                    return if (viewType == moviesAdapter.MOVIE_VIEW_TYPE) 1
                    else Utils.getScreenSizeLayout(requireContext())
                }
            }
            layoutManager = manager
            adapter = moviesAdapter
        }

        refresh_layout?.setOnRefreshListener {
            recycler_movies?.visibility = View.GONE
            text_recycler_error?.visibility = View.GONE
            progress?.visibility = View.GONE
            //viewModel.refreshBypassCache()
            refresh_layout?.isRefreshing = false
        }

        observeViewModel()
    }

    private fun observeViewModel() {

        viewModel.moviePagedList.observe(this, Observer {
            moviesAdapter.submitList(it)
        })

        viewModel.networkState.observe(this, Observer {
            progress?.visibility = if (viewModel.listIsEmpty() && it == NetworkState.LOADING) View.VISIBLE else View.GONE
            text_recycler_error?.visibility = if (viewModel.listIsEmpty() && it == NetworkState.ERROR) View.VISIBLE else View.GONE

            if (!viewModel.listIsEmpty()) {
                moviesAdapter.setNetworkState(it)
            }
        })

//        viewModel.movies.observe(this, Observer { movies ->
//            movies?.let {
//                recycler_movies?.visibility = View.VISIBLE
//                moviesAdapter.updateMovieList(movies)
//            }
//        })
//
//        viewModel.loading.observe(this, Observer { isLoading ->
//            isLoading?.let {
//                progress?.visibility = if (it) View.VISIBLE else View.GONE
//                if (it) {
//                    text_recycler_error?.visibility = View.GONE
//                    recycler_movies?.visibility = View.GONE
//                }
//            }
//        })
//
//        viewModel.loadError.observe(this, Observer { isError ->
//            isError?.let {
//                text_recycler_error?.visibility = if (it) View.VISIBLE else View.GONE
//            }
//        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.list_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        /*when (item.itemId) {
            R.id.action_settings -> {
                view?.let {
                    Navigation.findNavController(it)
                        .navigate(
                            ListMoviesFragmentDirections.actionListFragmentToSettingsFragment(),
                            NavUtils.navOptions
                        )
                }
            }
        }*/
        return super.onOptionsItemSelected(item)
    }

    private fun getViewModel(): ListMoviesViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return ListMoviesViewModel(Application(),  moviesRepository, serviceAPI) as T
            }
        })[ListMoviesViewModel::class.java]
    }
}
