package com.app4funbr.themoviedb.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.app4funbr.themoviedb.R
import com.app4funbr.themoviedb.databinding.ItemMovieBinding
import com.app4funbr.themoviedb.infrastructure.util.NavUtils.navOptions
import com.app4funbr.themoviedb.interfaces.ClickListener
import com.app4funbr.themoviedb.model.Movie
import com.app4funbr.themoviedb.repository.NetworkState
import com.app4funbr.themoviedb.view.fragments.ListMoviesFragmentDirections
import kotlinx.android.synthetic.main.item_movie.view.*
import kotlinx.android.synthetic.main.network_state_item.view.*
import java.util.zip.Inflater

class MoviesAdapter(private val movieList: ArrayList<Movie>) :
    PagedListAdapter<Movie, RecyclerView.ViewHolder>(MovieDiffCallback()) {

    val MOVIE_VIEW_TYPE = 1
    val NETWORK_VIEW_TYPE = 2

    private var networkState: NetworkState? = null

    fun updateMovieList(newMovieList: List<Movie>) {
        movieList.clear()
        movieList.addAll(newMovieList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        if (viewType == MOVIE_VIEW_TYPE) {
            val view = DataBindingUtil.inflate<ItemMovieBinding>(
                inflater,
                R.layout.item_movie,
                parent,
                false
            )
            return MovieItemViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.network_state_item, parent, false)
            return NetworkStateItemViewHolder(view)
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            NETWORK_VIEW_TYPE
        } else {
            MOVIE_VIEW_TYPE
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == MOVIE_VIEW_TYPE) {
            (holder as MovieItemViewHolder).bind(getItem(position)!!)
        } else {
            (holder as NetworkStateItemViewHolder).bind(networkState)
        }
    }

    private fun hasExtraRow(): Boolean {
        return networkState != null && networkState != NetworkState.LOADED
    }

    fun setNetworkState(networkState: NetworkState) {
        val previousState = this.networkState
        val hadExtraRow = hasExtraRow()
        this.networkState = networkState
        val hasExtraRow = hasExtraRow()

        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {
                notifyItemRemoved(super.getItemCount())
            } else {
                notifyItemInserted(super.getItemCount())
            }
        } else if (hadExtraRow && previousState != networkState) {
            notifyItemChanged(itemCount - 1)
        }
    }

    class MovieItemViewHolder(var view: ItemMovieBinding) : RecyclerView.ViewHolder(view.root), ClickListener {
        fun bind(movie: Movie) {
            view.movie = movie
            view.listener = this
        }

        override fun onClickListener(v: View) {
            val uuid = v.movieId.text.toString().toInt()
            val title = v.text_title.toString()
            val action = ListMoviesFragmentDirections.actionListFragmentToMovieDetailFragment()
            action.movieUuid = uuid
            action.title = title
            Navigation.findNavController(v)
                .navigate(action, navOptions)
        }
    }

    class NetworkStateItemViewHolder (view: View) : RecyclerView.ViewHolder(view) {

        fun bind(networkState: NetworkState?) {
            if (networkState != null && networkState == NetworkState.LOADING) {
                itemView.progress_bar_item.visibility = View.VISIBLE
            }
            else  {
                itemView.progress_bar_item.visibility = View.GONE
            }

            if (networkState != null && networkState == NetworkState.ERROR) {
                itemView.error_msg_item.visibility = View.VISIBLE
                itemView.error_msg_item.text = networkState.msg
            }
            else if (networkState != null && networkState == NetworkState.ENDOFLIST) {
                itemView.error_msg_item.visibility = View.VISIBLE
                itemView.error_msg_item.text = networkState.msg
            }
            else {
                itemView.error_msg_item.visibility = View.GONE
            }
        }
    }

    class MovieDiffCallback : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.id == newItem.id
        }
    }
}