package com.app4funbr.themoviedb.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.app4funbr.themoviedb.infrastructure.network.ServiceAPI
import com.app4funbr.themoviedb.model.Movie
import com.app4funbr.themoviedb.infrastructure.helper.MovieDatabase
import com.app4funbr.themoviedb.model.VideoResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch

class DetailMovieViewModel(application: Application): BaseViewModel(application) {

    val movieLiveData = MutableLiveData<Movie>()
    val videoUrl = MutableLiveData<String>()

    private val serviceAPI =
        ServiceAPI()
    private val disposable = CompositeDisposable()

    fun fetch(uuid: Int) {
        launch {
            val movie = MovieDatabase(
                getApplication()
            ).moviesDao().getMovie(uuid)
            movieLiveData.value = movie
        }
    }

    fun fetchMovieTrailer(id: Int) {
        disposable.add(
            serviceAPI.getMovieTrailer(id)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<VideoResponse>() {
                    override fun onSuccess(t: VideoResponse) {
                        Log.d("INFO", t.results.toString())
                        videoUrl.value = t.results[0].key
                    }

                    override fun onError(e: Throwable) {

                    }
                })
        )
    }
}