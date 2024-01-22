package com.primemedia.dooocustom.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.primemedia.dooocustom.fragments.LiquidPagerFragment


class LiquidPageradepter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {
    private val total: Int = 4
    private val data: ArrayList<LiquidPagerFragment> = ArrayList(total)

    init {
        for (i in 0 until total) {
            val fragment = LiquidPagerFragment()
            val bundle = Bundle()
            bundle.putInt("POSITION", i + 1)
            fragment.arguments = bundle
            data.add(fragment)
        }
    }


    override fun getItem(position: Int): Fragment {
        return data[position]
    }

    override fun getCount(): Int {
        return total
    }
}