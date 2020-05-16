package com.app4funbr.themoviedb.infrastructure.helper

import android.content.Context
import androidx.room.*
import com.app4funbr.themoviedb.infrastructure.util.Converters
import com.app4funbr.themoviedb.interfaces.MoviesDao
import com.app4funbr.themoviedb.model.Movie

@Database(entities = arrayOf(Movie::class), version = 1)
@TypeConverters(Converters::class)
abstract class MovieDatabase : RoomDatabase() {

    abstract fun moviesDao(): MoviesDao

    companion object {
        @Volatile private var instance: MovieDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance
            ?: synchronized(LOCK) {
            instance
                ?: buildDatabase(
                    context
                ).also{
                instance = it
            }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            MovieDatabase::class.java,
            "moviedatabase"
        ).build()
    }
}