package com.app4funbr.themoviedb.view.activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.app4funbr.themoviedb.infrastructure.network.POST_PER_PAGE
import com.app4funbr.themoviedb.infrastructure.network.ServiceAPI
import com.app4funbr.themoviedb.model.Movie
import com.app4funbr.themoviedb.repository.MovieDataSource
import com.app4funbr.themoviedb.repository.MovieDatasourceFactory
import com.app4funbr.themoviedb.repository.NetworkState
import io.reactivex.disposables.CompositeDisposable

class MoviePageListRepository(private val serviceAPI: ServiceAPI) {

    lateinit var moviePagedList: LiveData<PagedList<Movie>>
    lateinit var moviesDataSourceFactory: MovieDatasourceFactory

    fun fetchLiveMoviePagedList(compositeDisposable: CompositeDisposable)
            : LiveData<PagedList<Movie>> {

        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(POST_PER_PAGE)
            .build()

        moviePagedList = LivePagedListBuilder(moviesDataSourceFactory, config).build()
        return moviePagedList
    }

    fun getNetworkState(): LiveData<NetworkState> {
        return Transformations.switchMap<MovieDataSource, NetworkState>(
            moviesDataSourceFactory.moviesLiveDataSource, MovieDataSource::networkState
        )
    }
}