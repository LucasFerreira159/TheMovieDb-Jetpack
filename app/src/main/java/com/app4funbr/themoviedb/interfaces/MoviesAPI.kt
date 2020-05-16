package com.app4funbr.themoviedb.interfaces

import com.app4funbr.themoviedb.model.PaginatedResponse
import com.app4funbr.themoviedb.infrastructure.util.Constants
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface MoviesAPI {

    @GET("movie/popular?api_key=${Constants.API_KEY}&language=pt-BR")
    fun getPopularMovies(@Query("page") page: Int? = 1): Single<PaginatedResponse>
}