package com.demo.developer.deraesw.demomoviewes.ui

import android.graphics.drawable.Drawable
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.demo.developer.deraesw.demomoviewes.core.data.entity.Movie
import com.demo.developer.deraesw.demomoviewes.core.data.model.MovieInTheater
import com.demo.developer.deraesw.demomoviewes.core.data.model.UpcomingMovie
import com.demo.developer.deraesw.demomoviewes.extension.setAmountWithSuffix
import com.demo.developer.deraesw.demomoviewes.extension.setImageUrl
import com.demo.developer.deraesw.demomoviewes.utils.AppTools

object BindingAdapter {

    @BindingAdapter(value = ["posterImageUrl", "error", "placeHolder", "loaderRes"], requireAll = false)
    @JvmStatic fun setPosterImageUrl(
            imageView: ImageView,
            url : String?,
            errorRes : Drawable,
            placeholderRes: Drawable,
            loaderRes: Drawable? = null) {
        imageView.setImageUrl(
                url,
                AppTools.PosterSize.MEDIUM,
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
            placeholderRes: Drawable? = null) {
        imageView.setImageUrl(
                url,
                AppTools.ProfileSize.MEDIUM,
                errorRes,
                placeholderRes,
                isCircleCrop = true)
    }

    @BindingAdapter(value = ["backdropImageUrl", "error", "placeHolder", "loaderRes"], requireAll = false)
    @JvmStatic fun setBackdropImageUrl(
            imageView: ImageView,
            url: String?,
            errorRes : Drawable,
            placeholderRes: Drawable? = null,
            loaderRes: Drawable? = null) {
        imageView.setImageUrl(
                url,
                AppTools.BackdropSize.MEDIUM,
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

    @BindingAdapter("upcomingMovieGenre")
    @JvmStatic fun displayUpcomingMovieGenreList(
            textView: TextView,
            item: UpcomingMovie) {

        textView.text = item.genres.joinToString (transform = {it.name})
    }

    @BindingAdapter("movieDuration")
    @JvmStatic fun displayMovieDuration(
            textView: TextView,
            item: MovieInTheater) {

        textView.text = AppTools.convertMinuteToHours(item.runtime)
    }

    @BindingAdapter("upcomingMovieDuration")
    @JvmStatic fun displayUpcomingMovieDuration(
            textView: TextView,
            item: UpcomingMovie) {

        textView.text = AppTools.convertMinuteToHours(item.runtime)
    }

    @BindingAdapter("movieDurationMinute")
    @JvmStatic fun displayMovieDurationMinute(
            textView: TextView,
            item: Movie?) {

        textView.text = "${item?.runtime?.toString() ?: 0}"
    }

    @BindingAdapter("movieReleaseDate")
    @JvmStatic fun displayMovieReleaseDate(
            textView: TextView,
            item: Movie?) {

        item?.releaseDate?.apply {
            textView.text = AppTools.convertDateString(
                    this,
                    AppTools.DatePattern.MMMM_S_DD_C_YYYY
            )
        }
    }

    @BindingAdapter("upcomingMovieReleaseDate")
    @JvmStatic fun displayMovieReleaseDate(
            textView: TextView,
            item: UpcomingMovie?) {

        item?.releaseDate?.apply {
            textView.text = AppTools.convertDateString(
                    this,
                    AppTools.DatePattern.MMMM_S_DD_C_YYYY
            )
        }
    }

    @BindingAdapter("amountWithSuffix")
    @JvmStatic fun displayAmountWithSuffix(
            textView: TextView,
            value: Double) {
        textView.setAmountWithSuffix(value)
    }

    @BindingAdapter("amountWithSuffix")
    @JvmStatic fun displayAmountWithSuffix(
            textView: TextView,
            value: String) {
        textView.setAmountWithSuffix(value.toDouble())
    }
}