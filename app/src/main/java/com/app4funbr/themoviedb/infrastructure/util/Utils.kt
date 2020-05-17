package com.app4funbr.themoviedb.infrastructure.util

import android.content.Context
import android.content.res.Configuration
import android.util.DisplayMetrics
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

    fun getScreenSizeLayout(context: Context): GridLayoutManager {
        val metrics = DisplayMetrics()

        val yInches = metrics.heightPixels / metrics.ydpi
        val xInches = metrics.widthPixels / metrics.xdpi
        val diagonalInches = Math.sqrt((xInches * xInches + yInches * yInches).toDouble())

        var mLayoutManager: RecyclerView.LayoutManager

        if (diagonalInches >= 6.5) {
            //Verifica qual a orientação de tela, se for vertical irá gerar grid com 2 colunas, caso contrário 4
            if (context.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                mLayoutManager = GridLayoutManager(context, 3)
            } else {
                mLayoutManager = GridLayoutManager(context, 4)
            }
        } else {
            //Verifica qual a orientação de tela, se for vertical irá gerar grid com 2 colunas, caso contrário 4
            if (context.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                mLayoutManager = GridLayoutManager(context, 3)
            } else {
                mLayoutManager = GridLayoutManager(context, 5)
            }
        }

        return mLayoutManager
    }
}