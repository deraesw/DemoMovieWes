package com.demo.developer.deraesw.demomoviewes.extension

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

fun AppCompatActivity.showShortToast(message : String){
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}
