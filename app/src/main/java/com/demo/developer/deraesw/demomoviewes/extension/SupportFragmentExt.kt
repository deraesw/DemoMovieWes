package com.demo.developer.deraesw.demomoviewes.extension

import androidx.fragment.app.Fragment
import android.widget.Toast

fun androidx.fragment.app.Fragment.showShortToast(message : String){
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

fun androidx.fragment.app.Fragment.showLongToast(message : String){
    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
}