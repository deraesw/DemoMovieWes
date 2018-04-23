package com.demo.developer.deraesw.demomoviewes

import android.databinding.BindingAdapter
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
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