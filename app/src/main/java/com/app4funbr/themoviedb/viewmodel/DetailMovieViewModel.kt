package com.app4funbr.themoviedb.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.app4funbr.themoviedb.model.Movie
import com.app4funbr.themoviedb.util.MovieDatabase
import kotlinx.coroutines.launch

class DetailMovieViewModel(application: Application): BaseViewModel(application) {

    val movieLiveData = MutableLiveData<Movie>()

    fun fetch(uuid: Int) {
        launch {
            val movie = MovieDatabase(getApplication()).moviesDao().getMovie(uuid)
            movieLiveData.value = movie
        }
    }
}