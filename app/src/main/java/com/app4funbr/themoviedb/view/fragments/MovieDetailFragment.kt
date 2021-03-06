package com.app4funbr.themoviedb.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.app4funbr.themoviedb.R
import com.app4funbr.themoviedb.databinding.FragmentMovieDetailBinding
import com.app4funbr.themoviedb.infrastructure.util.Utils.getGenre
import com.app4funbr.themoviedb.viewmodel.DetailMovieViewModel
import kotlinx.android.synthetic.main.fragment_movie_detail.*

/**
 * A simple [Fragment] subclass.
 */
class MovieDetailFragment : Fragment() {

    private var url = ""
    private var movieUuid = 0
    private lateinit var viewModel: DetailMovieViewModel
    private lateinit var dataBinding: FragmentMovieDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_movie_detail, container, false)
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            movieUuid = MovieDetailFragmentArgs.fromBundle(it).movieUuid
        }

        viewModel = ViewModelProviders.of(this).get(DetailMovieViewModel::class.java)
        viewModel.fetch(movieUuid)

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.movieLiveData.observe(this, Observer { movie ->
            movie?.let {
                dataBinding.movie = it
                requireActivity().title = viewModel.movieLiveData.value?.title
                text_genere_detail?.text = getGenre(movie)
                viewModel.fetchMovieTrailer(movie.id)
            }
        })

        viewModel.videoUrl.observe(this, Observer { videoURL ->
            videoURL?.let {
                url = "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/$videoURL\" frameborder=\"0\" allowfullscreen></iframe>"
                setupVideoView(url)
            }
        })
    }

    private fun setupVideoView(url: String) {
        web_view?.settings?.javaScriptEnabled = true
        web_view?.webChromeClient = object : WebChromeClient() {}
        web_view?.loadData(url, "text/html", "utf-8")
    }
}
