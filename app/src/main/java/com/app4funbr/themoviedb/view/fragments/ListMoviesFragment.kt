package com.app4funbr.themoviedb.view.fragments

import android.os.Bundle
import android.os.Handler
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import android.widget.AbsListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.app4funbr.themoviedb.R
import com.app4funbr.themoviedb.infrastructure.util.NavUtils
import com.app4funbr.themoviedb.view.adapter.MoviesAdapter
import com.app4funbr.themoviedb.viewmodel.ListMoviesViewModel
import kotlinx.android.synthetic.main.fragment_list_movies.*

class ListMoviesFragment : Fragment() {

    private lateinit var viewModel: ListMoviesViewModel
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

        viewModel = ViewModelProviders.of(this).get(ListMoviesViewModel::class.java)
        viewModel.refresh()

        val resId = R.anim.grid_layout_animation_from_bottom
        val animation = AnimationUtils.loadLayoutAnimation(requireContext(), resId)

        recycler_movies?.apply {
            layoutAnimation = animation
            manager = GridLayoutManager(requireContext(), 3)
            layoutManager = manager
            adapter = moviesAdapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                        isScrolling = true
                    }
                }

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    currentItems = manager.childCount
                    totalItems = manager.itemCount
                    scrollOutItems = manager.findFirstVisibleItemPosition()

                    if (isScrolling && (currentItems + scrollOutItems == totalItems)) {
                        page++
                        viewModel.fetchOtherPagesFromRemote(page)
                    }
                }
            })
        }

        refresh_layout?.setOnRefreshListener {
            recycler_movies?.visibility = View.GONE
            text_recycler_error?.visibility = View.GONE
            progress?.visibility = View.GONE
            viewModel.refreshBypassCache()
            refresh_layout?.isRefreshing = false
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.movies.observe(this, Observer { movies ->
            movies?.let {
                recycler_movies?.visibility = View.VISIBLE
                moviesAdapter.updateMovieList(movies)
            }
        })

        viewModel.loading.observe(this, Observer { isLoading ->
            isLoading?.let {
                progress?.visibility = if (it) View.VISIBLE else View.GONE
                if (it) {
                    text_recycler_error?.visibility = View.GONE
                    recycler_movies?.visibility = View.GONE
                }
            }
        })

        viewModel.loadError.observe(this, Observer { isError ->
            isError?.let {
                text_recycler_error?.visibility = if (it) View.VISIBLE else View.GONE
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.list_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings -> {
                view?.let {
                    Navigation.findNavController(it)
                        .navigate(
                            ListMoviesFragmentDirections.actionListFragmentToSettingsFragment(),
                            NavUtils.navOptions
                        )
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun runAnimation() {
        val context = recycler_movies?.context
        val controller = AnimationUtils.loadLayoutAnimation(
            requireContext(),
            R.anim.grid_layout_animation_from_bottom
        )

        recycler_movies?.apply {
            layoutAnimation = controller
            adapter?.notifyDataSetChanged()
            scheduleLayoutAnimation()
        }
    }
}
