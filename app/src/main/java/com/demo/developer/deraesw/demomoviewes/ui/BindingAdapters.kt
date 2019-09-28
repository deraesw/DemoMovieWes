package com.demo.developer.deraesw.demomoviewes.ui

import androidx.databinding.BindingAdapter
import android.graphics.drawable.Drawable
import android.widget.ImageView
import android.widget.TextView
import com.demo.developer.deraesw.demomoviewes.data.model.MovieInTheater
import com.demo.developer.deraesw.demomoviewes.extension.setImageUrl
import com.demo.developer.deraesw.demomoviewes.utils.AppTools

object BindingAdapter {

    @BindingAdapter(value = ["posterImageUrl", "error", "placeHolder"])
    @JvmStatic fun setPosterImageUrl(
            imageView: ImageView,
            url : String?,
            errorRes : Drawable,
            placeholderRes: Drawable) {
        imageView.setImageUrl(
                url,
                AppTools.PosterSize.SMALL,
                errorRes,
                placeholderRes,
                isCenterCrop = true)
    }

    @BindingAdapter(value = ["logoImageUrl", "error", "placeHolder"])
    @JvmStatic fun setLogoImageUrl(
            imageView: ImageView,
            url : String?,
            errorRes : Drawable,
            placeholderRes: Drawable) {
        imageView.setImageUrl(
                url,
                AppTools.LogoSize.MEDIUM,
                errorRes,
                placeholderRes,
                isFitCenter = true)
    }

    @BindingAdapter(value = ["profileImageUrl", "error", "placeHolder"])
    @JvmStatic fun setProfileImageUrl(
            imageView: ImageView,
            url: String?,
            errorRes : Drawable,
            placeholderRes: Drawable) {
        imageView.setImageUrl(
                url,
                AppTools.ProfileSize.SMALL,
                errorRes,
                placeholderRes,
                isCircleCrop = true)
    }

    @BindingAdapter(value = ["backdropImageUrl", "error", "placeHolder"])
    @JvmStatic fun setBackdropImageUrl(
            imageView: ImageView,
            url: String?,
            errorRes : Drawable,
            placeholderRes: Drawable) {
        imageView.setImageUrl(
                url,
                AppTools.BackdropSize.SMALL,
                errorRes,
                placeholderRes,
                isCenterCrop = true)
    }

    @BindingAdapter("movieGenre")
    @JvmStatic fun displayMovieGenreList(
            textView: TextView,
            item: MovieInTheater) {

        textView.text = item.genres.joinToString (transform = {it.name})
    }

    @BindingAdapter("movieDuration")
    @JvmStatic fun displayMovieDuration(
            textView: TextView,
            item: MovieInTheater) {

        textView.text = AppTools.convertMinuteToHours(item.runtime)
    }
}