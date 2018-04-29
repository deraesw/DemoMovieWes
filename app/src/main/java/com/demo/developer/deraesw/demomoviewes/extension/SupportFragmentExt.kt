package com.demo.developer.deraesw.demomoviewes.extension

import android.support.v4.app.Fragment
import android.widget.Toast

fun Fragment.showShortToast(message : String){
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

fun Fragment.showLongToast(message : String){
    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
}