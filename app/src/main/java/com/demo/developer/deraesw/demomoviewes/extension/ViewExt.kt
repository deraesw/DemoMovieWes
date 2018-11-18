package com.demo.developer.deraesw.demomoviewes.extension

import android.graphics.drawable.Drawable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

fun TextView.setAmountWithSuffix(amount: Double) {
    var content = AppTools.convertAmountToSuffix(amount)
    if(content.isEmpty() || content == "0"){
        content = "unknown"
    }
    this.text = content
}

fun RecyclerView.setLinearLayout(
        fixedSize : Boolean = true,
        hasDivider : Boolean = true,
        isVertical : Boolean = true) {
    this.apply {
        setHasFixedSize(fixedSize)
        if(hasDivider) {
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
        layoutManager = LinearLayoutManager(
                context,
                if (isVertical) RecyclerView.VERTICAL else RecyclerView.HORIZONTAL,
                false)
    }
}