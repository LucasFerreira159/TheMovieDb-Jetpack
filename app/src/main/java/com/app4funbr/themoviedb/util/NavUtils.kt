package com.app4funbr.themoviedb.util

import androidx.navigation.NavOptions
import com.app4funbr.themoviedb.R

object NavUtils {

    val navOptions = NavOptions.Builder()
        .setEnterAnim(R.anim.slide_in_right)
        .setExitAnim(R.anim.slide_out_left)
        .setPopEnterAnim(R.anim.slide_in_left)
        .setExitAnim(R.anim.slide_out_right)
        .build()
}