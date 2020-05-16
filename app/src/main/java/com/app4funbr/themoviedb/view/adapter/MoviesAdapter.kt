package com.app4funbr.themoviedb.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.app4funbr.themoviedb.R
import com.app4funbr.themoviedb.databinding.ItemMovieBinding
import com.app4funbr.themoviedb.interfaces.ClickListener
import com.app4funbr.themoviedb.model.Movie

class MoviesAdapter(private val movieList: ArrayList<Movie>) :
    RecyclerView.Adapter<MoviesAdapter.ViewHolder>(),
    ClickListener {

    fun updateMovieList(newMovieList: List<Movie>) {
        movieList.clear()
        movieList.addAll(newMovieList)
        notifyDataSetChanged()
    }

    override fun onClickListener(v: View) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view =
            DataBindingUtil.inflate<ItemMovieBinding>(inflater, R.layout.item_movie, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
       return movieList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.view.movie = movieList[position]
        holder.view.listener = this
    }

    class ViewHolder(var view: ItemMovieBinding) : RecyclerView.ViewHolder(view.root)
}