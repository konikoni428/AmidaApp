package com.masahiro.amidaapp.ui.main

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter


/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
class SectionsPagerAdapter(private val context: Context, fm: FragmentManager, val startDataset: Array<String>) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        if(position < startDataset.size){
            return PlaceholderFragment.newInstance(position + 1)
        }else{
            return ResultListFragment.newInstance()
        }

    }

    override fun getPageTitle(position: Int): CharSequence? {
        if(position < startDataset.size){
            return startDataset[position]
        }
        return "一覧表示"
    }

    override fun getCount(): Int {
        // Show 2 total pages.
        return startDataset.size + 1
    }
}