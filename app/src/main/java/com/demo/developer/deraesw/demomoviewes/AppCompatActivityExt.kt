package com.demo.developer.deraesw.demomoviewes

import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.widget.Toast

fun AppCompatActivity.showShortToast(message : String){
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun AppCompatActivity.addFragmentToActivity(container : Int, fragment: Fragment) {
    supportFragmentManager.beginTransaction().add(container, fragment).commit()
}

fun AppCompatActivity.replaceFragmentToActivity(container : Int, fragment: Fragment) {
    supportFragmentManager.beginTransaction().replace(container, fragment).commit()
}