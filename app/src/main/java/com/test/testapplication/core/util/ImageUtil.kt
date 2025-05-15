package com.test.testapplication.core.util

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy

object ImageUtil {

    fun setImage(view: ImageView, imageUrl: String?) {
        Glide.with(view.context)
            .load(imageUrl)
            .dontAnimate()
            .priority(Priority.IMMEDIATE)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(view)
    }
}