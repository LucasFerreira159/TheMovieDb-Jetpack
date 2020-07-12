package com.app4funbr.themoviedb.interfaces

import com.app4funbr.themoviedb.model.PaginatedResponse
import com.app4funbr.themoviedb.infrastructure.util.Constants
import com.app4funbr.themoviedb.model.VideoResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MoviesAPI {

    @GET("movie/popular?api_key=${Constants.API_KEY}&language=pt-BR")
    fun getPopularMovies(@Query("page") page: Long): Single<PaginatedResponse>

    @GET("movie/{id}/videos?api_key=${Constants.API_KEY}&append_to_response=videos")
    fun getMovieTrailer(@Path("id") id: Int): Single<VideoResponse>
}