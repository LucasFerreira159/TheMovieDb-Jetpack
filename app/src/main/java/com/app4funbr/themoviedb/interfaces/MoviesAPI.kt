package com.app4funbr.themoviedb.interfaces

import com.app4funbr.themoviedb.model.PaginatedResponse
import com.app4funbr.themoviedb.infrastructure.util.Constants
import io.reactivex.Single
import retrofit2.http.GET

interface MoviesAPI {

    @GET("movie/popular?api_key=${Constants.API_KEY}&language=pt-BR")
    fun getPopularMovies(): Single<PaginatedResponse>
}