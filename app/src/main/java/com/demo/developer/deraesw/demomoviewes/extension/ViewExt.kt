package com.demo.developer.deraesw.demomoviewes.extension

import android.graphics.drawable.Drawable
import android.support.annotation.DrawableRes
import android.support.constraint.Placeholder
import android.widget.ImageView
import android.widget.TextView
import com.demo.developer.deraesw.demomoviewes.GlideApp
import com.demo.developer.deraesw.demomoviewes.R
import com.demo.developer.deraesw.demomoviewes.utils.AppTools

fun ImageView.setImageUrl(url: String?, size: String, errorRes : Drawable, placeholderRes: Drawable, isCenterCrop : Boolean = false, isFitCenter : Boolean = false, isCircleCrop : Boolean = false) {
    if(url != null && url != ""){
        val glide = GlideApp
                .with(context)
                .load(AppTools.getUrlStringForImage(url, size))
                .error(errorRes)
                .placeholder(placeholderRes)

        if(isCenterCrop) {
            glide.centerCrop()
        }
        if(isFitCenter){
            glide.fitCenter()
        }
        if(isCircleCrop){
            glide.circleCrop()
        }

        glide.into(this)

    } else {
        GlideApp.with(context)
                .load(placeholderRes)
                .into(this)
    }
}

fun TextView.setAmountWithSuffix(amount: Double){
    var content = AppTools.convertAmountToSuffix(amount)
    if(content.isEmpty() || content == "0"){
        content = "unknown"
    }
    this.text = content
}