package com.app4funbr.themoviedb.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class Movie (
    @ColumnInfo(name = "id")
    @SerializedName("id")
    val id: Int,

    @ColumnInfo(name = "popularity")
    @SerializedName("popularity")
    val popularity: Double,

    @ColumnInfo(name = "vote_count")
    @SerializedName("vote_count")
    val voteCount: Int,

    @ColumnInfo(name = "video")
    @SerializedName("video")
    val video: Boolean,

    @ColumnInfo(name = "poster_path")
    @SerializedName("poster_path")
    val posterPath: String,

    @ColumnInfo(name = "original_title")
    @SerializedName("original_title")
    val originalTitle: String,

    @ColumnInfo(name = "title")
    @SerializedName("title")
    val title: String,

    @ColumnInfo(name = "overview")
    @SerializedName("overview")
    val overview: String,

    @ColumnInfo(name = "release_date")
    @SerializedName("release_date")
    val releaseDate: String

    /*,
    @SerializedName("genre_ids")
    val genreId: List<Genre>*/
) {
    @PrimaryKey(autoGenerate = true)
    var uuid: Int = 0
}