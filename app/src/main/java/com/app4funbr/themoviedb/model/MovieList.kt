package com.app4funbr.themoviedb.model

import com.google.gson.annotations.SerializedName

data class MovieList (
    val popularity: Double,
    @SerializedName("vote_count")
    val voteCount: Int,
    val video: Boolean,
    @SerializedName("poster_path")
    val posterPath: String,
    @SerializedName("original_title")
    val originalTitle: String,
    val title: String,
    val overview: String,
    @SerializedName("release_date")
    val releaseDate: String

    /*,
    @SerializedName("genre_ids")
    val genreId: List<Genre>*/
)