package com.abdotareq.subway_e_ticketing.ui.fragment.settings

import android.widget.Button
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.abdotareq.subway_e_ticketing.R
import com.abdotareq.subway_e_ticketing.data.model.User
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions

/**
 * Uses the Glide library to load an image by URL into an [ImageView]
 */
@BindingAdapter("profileImageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?) {
    imgUrl?.let {
        val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()
        Glide.with(imgView.context)
                .load(imgUri)
                .apply(RequestOptions()
                        .placeholder(R.drawable.create_account_image)
//                        .error(R.drawable.ic_broken_image)
                        .override(100, 100) // resize does not respect aspect ratio
                        // for changing profile picture to work
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)

                )
                .into(imgView)
    }
}

@BindingAdapter("setBirthDate")
fun Button.setBirthDate(item: User?) {
    item?.let {
        text = it.birth_date!!.substring(0..9)
    }
}


