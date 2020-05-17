package com.app4funbr.themoviedb.infrastructure.network

import com.app4funbr.themoviedb.interfaces.MoviesAPI
import com.app4funbr.themoviedb.model.PaginatedResponse
import com.app4funbr.themoviedb.model.VideoResponse
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL = "https://api.themoviedb.org/3/"

class ServiceAPI {

    private val api = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(MoviesAPI::class.java)

    fun getPopularMovies(page: Int? = null): Single<PaginatedResponse> {
        return api.getPopularMovies(page)
    }

    fun getMovieTrailer(id: Int) : Single<VideoResponse> {
        return api.getMovieTrailer(id)
    }
}