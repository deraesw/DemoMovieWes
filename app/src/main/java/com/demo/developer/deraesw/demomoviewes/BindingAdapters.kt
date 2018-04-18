package com.demo.developer.deraesw.demomoviewes

import android.databinding.BindingAdapter
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import com.demo.developer.deraesw.demomoviewes.utils.AppTools

fun ImageView.setImageUrl(url: String?, size: String) {
    if(url != null && url != ""){
        GlideApp.with(context)
                .load(AppTools.getUrlStringForImage(url, size))
                .centerCrop()
                .into(this)
    } else {
        GlideApp.with(context)
                .clear(this)
    }
}

fun TextView.setAmountWithSuffix(amount: Double){
    var content = AppTools.convertAmountToSuffix(amount)
    if(content.isEmpty() || content == "0"){
        content = "unknown"
    }
    this.text = content
}