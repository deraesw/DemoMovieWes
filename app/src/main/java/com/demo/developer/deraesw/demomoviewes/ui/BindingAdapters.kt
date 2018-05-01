package com.demo.developer.deraesw.demomoviewes.ui

import android.databinding.BindingAdapter
import android.widget.ImageView
import com.demo.developer.deraesw.demomoviewes.extension.setImageUrl
import com.demo.developer.deraesw.demomoviewes.extension.setRoundImageUrl
import com.demo.developer.deraesw.demomoviewes.utils.AppTools

object BindingAdapter {

    @BindingAdapter("app:posterImageUrl")
    @JvmStatic fun setPosterImageUrl(imageView: ImageView, url : String?){
        imageView.setImageUrl(url, AppTools.PosterSize.SMALL)
    }

    @BindingAdapter("app:profileImageUrl")
    @JvmStatic fun setProfileImageUrl(imageView: ImageView, url: String?){
        imageView.setRoundImageUrl(url, AppTools.ProfileSize.SMALL)
    }
}