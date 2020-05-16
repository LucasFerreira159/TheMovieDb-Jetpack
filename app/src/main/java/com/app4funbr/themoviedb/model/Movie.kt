package com.app4funbr.themoviedb.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class Movie(
    @ColumnInfo(name = "id")
    @SerializedName("id")
    val id: Int,

    @ColumnInfo(name = "total_pages")
    @SerializedName("total_pages")
    val totalPages: Int,

    @ColumnInfo(name = "vote_average")
    @SerializedName("vote_average")
    val voteAverage: Double,

    @ColumnInfo(name = "video")
    @SerializedName("video")
    val video: Boolean,

    @ColumnInfo(name = "poster_path")
    @SerializedName("poster_path")
    val posterPath: String? = null,

    @ColumnInfo(name = "backdrop_path")
    @SerializedName("backdrop_path")
    val backdropPath: String? = null,

    @ColumnInfo(name = "original_title")
    @SerializedName("original_title")
    val originalTitle: String,

    @ColumnInfo(name = "title")
    @SerializedName("title")
    val title: String,

    @ColumnInfo(name = "genre_ids")
    @SerializedName("genre_ids")
    val genre: List<Int>? = null,

    @ColumnInfo(name = "overview")
    @SerializedName("overview")
    val overview: String,

    @ColumnInfo(name = "release_date")
    @SerializedName("release_date")
    val releaseDate: String
) {
    @PrimaryKey(autoGenerate = true)
    var uuid: Int = 0
}