package com.demo.developer.deraesw.demomoviewes.extension

import android.widget.ImageView
import android.widget.TextView
import com.demo.developer.deraesw.demomoviewes.GlideApp
import com.demo.developer.deraesw.demomoviewes.R
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

fun ImageView.setRoundImageUrl(url: String?, size: String) {
    if(url != null && url != ""){
        GlideApp.with(context)
                .load(AppTools.getUrlStringForImage(url, size))
                .circleCrop()
                .error(R.drawable.ic_account_circle_black_24dp)
                .placeholder(R.drawable.ic_account_circle_black_24dp)
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