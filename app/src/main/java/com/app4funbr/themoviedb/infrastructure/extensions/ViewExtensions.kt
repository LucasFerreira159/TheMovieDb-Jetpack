package com.app4funbr.themoviedb.infrastructure.extensions

import android.content.Context
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.app4funbr.themoviedb.R
import com.app4funbr.themoviedb.infrastructure.util.Constants
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions

fun getProgressDrawable(context: Context): CircularProgressDrawable {
    return CircularProgressDrawable(context).apply {
        strokeWidth = 10f
        centerRadius = 50f
        start()
    }
}

fun ImageView.loadImage(uri: String?, progressDrawable: CircularProgressDrawable) {
    val options = RequestOptions()
        .placeholder(progressDrawable)
        .error(R.mipmap.ic_launcher)
    Glide.with(context)
        .setDefaultRequestOptions(options)
        .load(uri)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .into(this)
}

@BindingAdapter("android:imageUrlBackdrop")
fun loadImageBackdrop(view: ImageView, url: String?) {
    view.loadImage(Constants.BACKDROP_URL + url, getProgressDrawable(view.context))
}


@BindingAdapter("android:imageUrl")
fun loadImage(view: ImageView, url: String?) {
    view.loadImage(Constants.POSTER_URL + url, getProgressDrawable(view.context))
}


fun <T> MutableLiveData<T>.notifyObserver() {
    this.value = this.value
}