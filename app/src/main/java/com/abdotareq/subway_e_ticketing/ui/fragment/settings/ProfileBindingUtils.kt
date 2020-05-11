package com.abdotareq.subway_e_ticketing.ui.fragment.settings

import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.abdotareq.subway_e_ticketing.R
import com.abdotareq.subway_e_ticketing.model.User
import com.bumptech.glide.Glide
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
                        .placeholder(R.drawable.loading_animation)
//                        .error(R.drawable.ic_broken_image)
                        .override(100, 100) // resize does not respect aspect ratio

                )
                .into(imgView)
    }
}

@BindingAdapter("setFirstName")
fun EditText.setFirstName(item: User?) {
    item?.let {
        setText(it.first_name)
    }
}

@BindingAdapter("setLastName")
fun EditText.setLastName(item: User?) {
    item?.let {
        setText(it.last_name)
    }
}

@BindingAdapter("setMail")
fun TextView.setMail(item: User?) {
    item?.let {
        text = it.email
    }
}

@BindingAdapter("setGender")
fun Button.setGender(item: User?) {
    item?.let {
        text = it.gender
    }
}

@BindingAdapter("setBirthDate")
fun Button.setBirthDate(item: User?) {
    item?.let {
        text = it.birth_date!!.substring(0..9)
    }
}


