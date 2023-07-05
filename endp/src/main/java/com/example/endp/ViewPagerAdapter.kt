package com.example.endp

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class MyPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        // 페이지 수를 반환합니다.
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        // 해당 위치의 프래그먼트를 생성하여 반환합니다.
        return when (position) {
//            0 -> Fragment1()
//            1 -> Fragment2()
//            2 -> Fragment3()
            else -> throw IllegalArgumentException("Invalid position: $position")
        }
    }
}