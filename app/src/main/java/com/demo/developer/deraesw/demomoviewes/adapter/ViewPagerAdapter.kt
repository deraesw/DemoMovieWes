package com.demo.developer.deraesw.demomoviewes.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import java.util.*

class ViewPagerAdapter(manager: androidx.fragment.app.FragmentManager) : androidx.fragment.app.FragmentStatePagerAdapter(manager) {
    private val mFragmentList = ArrayList<androidx.fragment.app.Fragment>()
    private val mFragmentTitleList = ArrayList<String>()

    override fun getItem(position: Int) =  mFragmentList[position]

    override fun getCount() = mFragmentList.size

    override fun getPageTitle(position: Int) = mFragmentTitleList[position]

    fun addFragment(fragment: androidx.fragment.app.Fragment, title: String) {
        mFragmentList.add(fragment)
        mFragmentTitleList.add(title)
    }

    fun clear() {
        mFragmentList.clear()
        mFragmentTitleList.clear()
    }
}