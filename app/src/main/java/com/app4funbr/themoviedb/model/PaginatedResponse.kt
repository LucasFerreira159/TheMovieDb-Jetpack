package com.app4funbr.themoviedb.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PaginatedResponse (
    @Expose
    @SerializedName("page")
    val page: Int,
    @Expose
    @SerializedName("total_results")
    val totalResults: String?,
    @Expose
    @SerializedName("total_pages")
    val totalPages: String?,
    @Expose
    @SerializedName("results")
    val results: MutableList<Movie>
)
