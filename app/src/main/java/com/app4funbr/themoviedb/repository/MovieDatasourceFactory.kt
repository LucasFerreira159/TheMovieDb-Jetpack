package com.app4funbr.themoviedb.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.app4funbr.themoviedb.infrastructure.network.ServiceAPI
import com.app4funbr.themoviedb.model.Movie
import io.reactivex.disposables.CompositeDisposable

class MovieDatasourceFactory(
    private val serviceAPI: ServiceAPI,
    private val compositeDisposable: CompositeDisposable
) : DataSource.Factory<Long, Movie>() {

    val moviesLiveDataSource = MutableLiveData<MovieDataSource>()

    override fun create(): DataSource<Long, Movie> {
        val movieDataSource = MovieDataSource(serviceAPI, compositeDisposable)
        moviesLiveDataSource.postValue(movieDataSource)
        return movieDataSource
    }
}