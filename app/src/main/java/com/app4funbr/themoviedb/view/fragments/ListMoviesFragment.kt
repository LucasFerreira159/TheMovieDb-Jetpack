package com.app4funbr.themoviedb.view.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager

import com.app4funbr.themoviedb.R
import com.app4funbr.themoviedb.util.NavUtils
import com.app4funbr.themoviedb.view.adapter.MoviesAdapter
import com.app4funbr.themoviedb.viewmodel.ListMoviesViewModel
import kotlinx.android.synthetic.main.fragment_list_movies.*

class ListMoviesFragment : Fragment() {

    private lateinit var viewModel: ListMoviesViewModel
    private val moviesAdapter = MoviesAdapter(arrayListOf())

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

        recycler_movies?.apply {
            layoutManager = GridLayoutManager(requireContext(), 3)
            adapter = moviesAdapter
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
}
