package com.demo.developer.deraesw.demomoviewes.utils

import androidx.test.espresso.matcher.ViewMatchers
import org.hamcrest.CoreMatchers

fun Any.assertEqualTo(condition: Any) {
    ViewMatchers.assertThat(this, CoreMatchers.equalTo(condition))
}