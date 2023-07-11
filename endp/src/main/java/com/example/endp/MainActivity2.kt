package com.example.endp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity2 : AppCompatActivity() {
    private lateinit var viewPager: ViewPager2
    private lateinit var vpAdapter: CustomPagerAdapter
    private lateinit var button_logout: Button
    private lateinit var map: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        viewPager = findViewById(R.id.viewPager)
        vpAdapter = CustomPagerAdapter(supportFragmentManager, lifecycle)
        viewPager.adapter = vpAdapter

        button_logout = findViewById(R.id.button_logout)

        button_logout.setOnClickListener {
            performLogout()

        }
        map = findViewById(R.id.map)

        map.setOnClickListener {
            val mapIntent = Intent(this@MainActivity2, MapActivity::class.java)
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

    private fun performLogout() {
        // 여기에 로그아웃 처리를 구현합니다.
        // 로그아웃을 수행하는 API 호출이 필요한 경우, Retrofit을 사용하여 API 호출을 수행합니다.
        // 예를 들어, 서버에 로그아웃 요청을 보내는 API가 있다면 아래와 같이 호출할 수 있습니다.

        // Retrofit 인스턴스 생성
        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.100.57:8090")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        // API 서비스 인터페이스 생성
        val apiService = retrofit.create(ApiService::class.java)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiService.logout().execute()
                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) {
                        // 로그아웃 성공 처리
                        Toast.makeText(applicationContext, "로그아웃 성공", Toast.LENGTH_SHORT).show()
                        navigateToLogin()
                    }
                } else {
                    // API 호출 실패 처리
                    withContext(Dispatchers.Main) {
                        val errorBody: String? = response.errorBody()?.string()
                        Toast.makeText(applicationContext, "API 호출 실패: $errorBody", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                // 예외 처리
                withContext(Dispatchers.Main) {
                    Log.e("kong", "API 호출 실패: ${e.message}")
                    Toast.makeText(applicationContext, "API 호출 실패", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun navigateToLogin() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish() // Optional: Finish the MainActivity to prevent going back
    }
}