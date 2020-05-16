package com.app4funbr.themoviedb.util

import android.content.Context
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.navigation.NavOptions
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.app4funbr.themoviedb.R
import com.bumptech.glide.Glide
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
        .into(this)
}

@BindingAdapter("android:imageUrl")
fun loadImage(view: ImageView, url: String?) {
    view.loadImage(Constants.POSTER_URL + url, getProgressDrawable(view.context))
}

@BindingAdapter("android:imageUrlBackdrop")
fun loadImageBackdrop(view: ImageView, url: String?) {
    view.loadImage(Constants.BACKDROP_URL + url, getProgressDrawable(view.context))
}