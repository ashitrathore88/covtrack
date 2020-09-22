package com.covid.covtrack.adapters


import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.covid.covtrack.views.fragments.NewsFragment
import com.covid.covtrack.views.fragments.VideosFragment

class NewsFragmentTabAdaptor (activity: Fragment, internal var totalTabs: Int)
    :FragmentStateAdapter(activity){
    private val arrayList: ArrayList<Fragment> = ArrayList()
    fun addFragment(fragment: Fragment?) {
        arrayList.add(fragment!!)
    }
    override fun getItemCount(): Int {
        return totalTabs
    }

    override fun createFragment(position: Int): Fragment {
        return        arrayList.get(position);
        //return NewsFragment()
//        when (position) {
//            0 -> return VideosFragment()
//            1 -> return NewsFragment()
//            2 -> return NewsFragment()
//           else -> return Fragment()
//        }


    }

}