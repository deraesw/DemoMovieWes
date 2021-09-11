package com.demo.developer.deraesw.demomoviewes.core

import androidx.test.espresso.matcher.ViewMatchers
import org.hamcrest.CoreMatchers

fun Boolean.assertIsFalse() {
    ViewMatchers.assertThat(this, CoreMatchers.equalTo(false))
}

fun Boolean.assertIsTrue() {
    ViewMatchers.assertThat(this, CoreMatchers.equalTo(true))
}

fun <T> T.assertEqualTo(value: T) {
    ViewMatchers.assertThat(this, CoreMatchers.equalTo(value))
}