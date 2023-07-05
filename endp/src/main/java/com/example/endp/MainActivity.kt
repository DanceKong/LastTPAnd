package com.example.endp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2


class MainActivity : AppCompatActivity() {

        private lateinit var viewPager: ViewPager2
        private lateinit var vpAdapter: CustomPagerAdapter
        private lateinit var button_login: Button
        private lateinit var map: Button

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_main)

            viewPager = findViewById(R.id.viewPager)
            vpAdapter = CustomPagerAdapter(supportFragmentManager, lifecycle)
            viewPager.adapter = vpAdapter

            button_login = findViewById(R.id.button_login)

            button_login.setOnClickListener {
                val myIntent = Intent(this@MainActivity, LoginActivity::class.java)
                startActivity(myIntent)
                finish()
            }
            map = findViewById(R.id.map)

            map.setOnClickListener {
                val mapIntent = Intent(this@MainActivity, MapActivity::class.java)
                startActivity(mapIntent)
                finish()
            }

        }


        class CustomPagerAdapter(fm: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fm, lifecycle) {
            private val PAGENUMBER = 4

            override fun getItemCount(): Int {
                return PAGENUMBER
            }

            override fun createFragment(position: Int): Fragment {
                return when (position) {
                    0 -> Fragment1.newInstance(R.drawable.takealook, "test 00")
                    1 -> Fragment1.newInstance(R.drawable.takealook, "test 01")
                    2 -> Fragment1.newInstance(R.drawable.takealook, "test 02")
                    else -> Fragment1.newInstance(R.drawable.takealook, "test 03")
                }
            }
        }

}
