package com.app4funbr.themoviedb.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.app4funbr.themoviedb.infrastructure.extensions.notifyObserver
import com.app4funbr.themoviedb.infrastructure.helper.MovieDatabase
import com.app4funbr.themoviedb.infrastructure.helper.SharedPreferencesHelper
import com.app4funbr.themoviedb.infrastructure.network.ServiceAPI
import com.app4funbr.themoviedb.model.Movie
import com.app4funbr.themoviedb.repository.NetworkState
import com.app4funbr.themoviedb.view.activity.MoviePageListRepository
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.launch

class ListMoviesViewModel(
    application: Application,
    private val movieRepository: MoviePageListRepository,
    private val serviceAPI: ServiceAPI
) : BaseViewModel(application) {

    /*private var prefHelper =
        SharedPreferencesHelper(
            getApplication()
        )*/

    private val disposable = CompositeDisposable()

    val moviePagedList: LiveData<PagedList<Movie>> by  lazy {
        movieRepository.fetchLiveMoviePagedList(disposable)
    }

    val networkState: LiveData<NetworkState> by lazy {
        movieRepository.getNetworkState()
    }

    //5 minutos em nanosegundos
    private var refreshTime = 5 * 60 * 1000 * 1000 * 1000L

    val movies = MutableLiveData<MutableList<Movie>>()
    val loading = MutableLiveData<Boolean>()
    val loadError = MutableLiveData<Boolean>()

    /*fun refresh() {
        loading.value = true
        checkCacheDuration()
        val updateTime = prefHelper.getUpdateTime()
        if (updateTime != null &&
            updateTime != 0L &&
            System.nanoTime() - updateTime < refreshTime
        ) {
            fetchFromDatabase()
        } else {
            //fetchFromRemote()
        }
    }

    private fun checkCacheDuration() {
        val cachePreference = prefHelper.getCacheDuration()

        try {
            val cachePreferenceInt = cachePreference?.toInt() ?: 5 * 60
            refreshTime = cachePreferenceInt.times(1000 * 1000 * 1000L)
        } catch (e: NumberFormatException) {
            e.printStackTrace()
        }
    }*/

    fun refreshBypassCache() {
        //fetchFromRemote()
    }

    /*private fun fetchFromRemote() {
        loading.value = true
        disposable.add(
            serviceAPI.getPopularMovies(null)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<PaginatedResponse>() {
                    override fun onSuccess(t: PaginatedResponse) {
                        storeMoviesLocally(t.results)
                        loading.value = false
                        Toast.makeText(
                            getApplication(),
                            "Recuperando informações do endpoint",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    override fun onError(e: Throwable) {
                        loadError.value = true
                        loading.value = false
                    }
                })
        )
    }*/

   /* private fun fetchFromDatabase() {
        loading.value = true
        launch {
            val movies = MovieDatabase(
                getApplication()
            ).moviesDao().getAllMovies()
            moviesRetrieved(movies)
        }
    }

    private fun moviesRetrieved(movieList: MutableList<Movie>) {
        movies.value = movieList
        loadError.value = false
        loading.value = false
    }

    private fun storeMoviesLocally(list: MutableList<Movie>) {
        launch {
            val dao = MovieDatabase(
                getApplication()
            ).moviesDao()
            dao.deleteAllMovies()
            val result = dao.insertAll(*list.toTypedArray())
            var i = 0
            while (i < list.size) {
                list[i].uuid = result[i].toInt()
                ++i
            }
            moviesRetrieved(list)
        }
        prefHelper.saveUpdateTime(System.nanoTime())
    }*/

    /*fun fetchOtherPagesFromRemote(page: Int? = null) {
        disposable.add(
            serviceAPI.getPopularMovies(page)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<PaginatedResponse>() {
                    override fun onSuccess(t: PaginatedResponse) {
                        storeOtherPagesLocally(t.results)
                    }

                    override fun onError(e: Throwable) {
                        loadError.value = true
                        loading.value = false
                    }
                })
        )
    }*/

    /*private fun storeOtherPagesLocally(list: MutableList<Movie>) {
        launch {
            val dao = MovieDatabase(
                getApplication()
            ).moviesDao()
            val result = dao.insertAll(*list.toTypedArray())
            var i = 0
            while (i < list.size) {
                list[i].uuid = result[i].toInt()
                ++i
            }

            movies.value?.addAll(list)
            movies.notifyObserver()
        }
        prefHelper.saveUpdateTime(System.nanoTime())
    }*/

    fun listIsEmpty(): Boolean {
        return moviePagedList.value?.isEmpty() ?: true
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}