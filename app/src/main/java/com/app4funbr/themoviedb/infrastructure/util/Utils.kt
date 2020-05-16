package com.app4funbr.themoviedb.infrastructure.util

import com.app4funbr.themoviedb.model.Movie
import com.app4funbr.themoviedb.model.enum.Genre

object Utils {

    fun getGenre(movie: Movie): String {
        val builder = StringBuilder()
        val genre: List<Int> = movie.genre!!
        genre?.let { genre ->
            for (i in genre) {
                when(i) {
                    Genre.Acao.genre -> builder.append("Ação ")
                    Genre.Aventura.genre -> builder.append("Aventura ")
                    Genre.Comedia.genre -> builder.append("Comédia ")
                    Genre.Crime.genre -> builder.append("Crime ")
                    Genre.Documentario.genre -> builder.append("Documentário ")
                    Genre.Drama.genre -> builder.append("Drama ")
                    Genre.Familia.genre -> builder.append("Família ")
                    Genre.Fantasia.genre -> builder.append("Fantasia ")
                    Genre.Historia.genre -> builder.append("História ")
                    Genre.Terror.genre -> builder.append("Terror ")
                    Genre.Musica.genre -> builder.append("Musica ")
                    Genre.Misterio.genre -> builder.append("Mistério ")
                    Genre.Romance.genre -> builder.append("Romance ")
                    Genre.Ficcao.genre -> builder.append("Ficção ")
                    Genre.CinemaTV.genre -> builder.append("Cinema TV ")
                    Genre.Thriller.genre -> builder.append("Thriller ")
                    Genre.Guerra.genre -> builder.append("Guerra ")
                    Genre.Faroeste.genre -> builder.append("Faroeste ")
                }
            }
        }
        return builder.toString()
    }

}