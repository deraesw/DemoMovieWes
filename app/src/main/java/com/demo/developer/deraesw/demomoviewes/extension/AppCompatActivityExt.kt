package com.demo.developer.deraesw.demomoviewes.extension

import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast

fun AppCompatActivity.showShortToast(message : String){
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun AppCompatActivity.addFragmentToActivity(container : Int, fragment: androidx.fragment.app.Fragment) {
    supportFragmentManager.beginTransaction().add(container, fragment).commit()
}

fun AppCompatActivity.replaceFragmentToActivity(container : Int, fragment: androidx.fragment.app.Fragment) {
    supportFragmentManager.beginTransaction().replace(container, fragment).commit()
}