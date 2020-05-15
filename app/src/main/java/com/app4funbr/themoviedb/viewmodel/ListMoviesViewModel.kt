package com.app4funbr.themoviedb.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.app4funbr.themoviedb.ServiceAPI
import com.app4funbr.themoviedb.model.MovieList
import com.app4funbr.themoviedb.model.PaginatedResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class ListMoviesViewModel(application: Application): BaseViewModel(application) {

    private val serviceAPI = ServiceAPI()
    private val disposable = CompositeDisposable()

    val movies = MutableLiveData<MutableList<MovieList>>()
    val loading = MutableLiveData<Boolean>()
    val loadError = MutableLiveData<Boolean>()

    fun fetchFromRemote(key: String) {
        loading.value = true
        disposable.add(
            serviceAPI.getPopularMovies(key)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<PaginatedResponse>(){
                    override fun onSuccess(t: PaginatedResponse) {
                        //storeMoviesLocally(t.results)
                        movies.value = t.results
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
}