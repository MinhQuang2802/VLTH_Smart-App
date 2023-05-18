package com.example.kotlin_firebase.menu


import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.util.Log
import com.example.kotlin_firebase.R

class ViewPagerAdapter(fm: FragmentManager?) : FragmentStatePagerAdapter(fm) {
    override fun getCount(): Int {
        return 3
    }

    override fun getItem(p0: Int): Fragment {
        when (p0) {
            0-> {
               return foodFragment()
            }
            1 -> {
                return WateryFoodFragment()
            }
            2 -> {
                return DrinksFragment()
            }
        }
        return foodFragment()
    }
}