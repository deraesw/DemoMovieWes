package com.demo.developer.deraesw.demomoviewes.extension

import androidx.test.espresso.Espresso
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import org.hamcrest.CoreMatchers.not

fun Int.withId(): ViewInteraction = Espresso.onView(ViewMatchers.withId(this))

fun Int.withText(): ViewInteraction = Espresso.onView(ViewMatchers.withText(this))

fun ViewInteraction.isDisplay(): ViewInteraction = this.check(matches(isDisplayed()))

fun ViewInteraction.isNotDisplay(): ViewInteraction = this.check(matches(not(isDisplayed())))

fun ViewInteraction.isText(text: String): ViewInteraction = this.check(matches(withText(text)))

fun ViewInteraction.isText(resource: Int): ViewInteraction = this.check(matches(withText(resource)))