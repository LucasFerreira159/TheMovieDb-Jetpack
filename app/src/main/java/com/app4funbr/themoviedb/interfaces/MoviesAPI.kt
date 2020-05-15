package com.app4funbr.themoviedb.interfaces

import com.app4funbr.themoviedb.model.PaginatedResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface MoviesAPI {

    @GET("movie/popular")
    fun getPopularMovies(@Query("api_key") key: String): Single<PaginatedResponse>
}