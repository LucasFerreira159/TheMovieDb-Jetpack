package com.app4funbr.themoviedb.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.app4funbr.themoviedb.ServiceAPI
import com.app4funbr.themoviedb.infrastructure.extensions.notifyObserver
import com.app4funbr.themoviedb.model.Movie
import com.app4funbr.themoviedb.model.PaginatedResponse
import com.app4funbr.themoviedb.infrastructure.helper.MovieDatabase
import com.app4funbr.themoviedb.infrastructure.helper.SharedPreferencesHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch
import java.lang.NumberFormatException

class ListMoviesViewModel(application: Application) : BaseViewModel(application) {

    private var prefHelper =
        SharedPreferencesHelper(
            getApplication()
        )
    //5 minutos em nanosegundos
    private var refreshTime = 5 * 60 * 1000 * 1000 * 1000L

    private val serviceAPI = ServiceAPI()
    private val disposable = CompositeDisposable()

    val movies = MutableLiveData<MutableList<Movie>>()
    val loading = MutableLiveData<Boolean>()
    val loadError = MutableLiveData<Boolean>()

    fun refresh() {
        loading.value = true
        checkCacheDuration()
        val updateTime = prefHelper.getUpdateTime()
        if (updateTime != null &&
            updateTime != 0L &&
            System.nanoTime() - updateTime < refreshTime
        ) {
            fetchFromDatabase()
        } else {
            fetchFromRemote()
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
    }

    fun refreshBypassCache() {
        fetchFromRemote()
    }

    private fun fetchFromRemote() {
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
    }

    private fun fetchFromDatabase() {
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
    }

    fun fetchOtherPagesFromRemote(page: Int? = null) {
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
    }

    private fun storeOtherPagesLocally(list: MutableList<Movie>) {
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
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}