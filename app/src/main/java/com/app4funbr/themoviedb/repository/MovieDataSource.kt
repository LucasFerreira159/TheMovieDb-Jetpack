package com.app4funbr.themoviedb.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.app4funbr.themoviedb.infrastructure.network.FIRST_PAGE
import com.app4funbr.themoviedb.infrastructure.network.ServiceAPI
import com.app4funbr.themoviedb.model.Movie
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MovieDataSource(
    private val serviceAPI: ServiceAPI,
    private val compositeDisposable: CompositeDisposable
) : PageKeyedDataSource<Long, Movie>() {

    val networkState = MutableLiveData<NetworkState>()
    private var page = FIRST_PAGE

    override fun loadInitial(
        params: LoadInitialParams<Long>,
        callback: LoadInitialCallback<Long, Movie>
    ) {
        networkState.postValue(NetworkState.LOADING)
        compositeDisposable.add(
            serviceAPI.api.getPopularMovies(page.toLong())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    callback.onResult(it.results, null, (page + 1).toLong())
                    networkState.postValue(NetworkState.LOADED)
                }, {
                    networkState.postValue(NetworkState.ERROR)
                    Log.e("MovieDataSource", it.message ?: "Ocorreu um erro ao carregar dados")
                })
        )
    }

    override fun loadAfter(params: LoadParams<Long>, callback: LoadCallback<Long, Movie>) {
        networkState.postValue(NetworkState.LOADING)
        compositeDisposable.add(
            serviceAPI.api.getPopularMovies(params.key)
                .subscribeOn(Schedulers.io())
                .subscribe({
                    if (it.totalPages >= params.key) {
                        callback.onResult(it.results, params.key + 1)
                        networkState.postValue(NetworkState.LOADED)
                    } else {
                        networkState.postValue(NetworkState.ENDOFLIST)
                    }
                }, {
                    networkState.postValue(NetworkState.ERROR)
                    Log.e("MovieDataSource", it.message)
                })
        )
    }

    override fun loadBefore(params: LoadParams<Long>, callback: LoadCallback<Long, Movie>) {
        TODO("Not yet implemented")
    }
}