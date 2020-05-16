package com.app4funbr.themoviedb.interfaces

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.app4funbr.themoviedb.model.Movie

@Dao
interface MoviesDao {

    @Insert
    suspend fun insertAll(vararg movies: Movie): MutableList<Long>

    @Query("SELECT * FROM movie")
    suspend fun getAllMovies(): MutableList<Movie>

    @Query("SELECT * FROM movie WHERE uuid = :id")
    suspend fun getMovie(id: Int): Movie

    @Query("DELETE FROM movie")
    suspend fun deleteAllMovies()
}