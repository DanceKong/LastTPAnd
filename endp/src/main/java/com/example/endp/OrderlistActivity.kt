package com.example.endp

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class OrderlistActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_orderlist)


        fun getSessionInfo(context: Context): String? {
            val sharedPreferences: SharedPreferences =
                context.getSharedPreferences("session", Context.MODE_PRIVATE)
            return sharedPreferences.getString("name", null)
        }

        // 주문 목록 호출 함수
        fun getMyOrderList(context: Context) {
            val sessionInfo = getSessionInfo(context)

            // 세션 정보가 있을 경우에만 주문 목록을 호출
            sessionInfo?.let {
                val retrofit = Retrofit.Builder()
                    .baseUrl("http://192.168.100.57:8090")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                val myOrderService = retrofit.create(ApiService::class.java)

                val call = myOrderService.getMyOrder()
                call.enqueue(object : Callback<List<MenuOrder>> {
                    override fun onResponse(
                        call: Call<List<MenuOrder>>,
                        response: Response<List<MenuOrder>>
                    ) {
                        if (response.isSuccessful) {
                            val myOrderList = response.body()
                            // myOrderList를 사용하여 원하는 작업 수행
                        } else {
                            // 요청이 실패한 경우 처리할 내용
                        }
                    }

                    override fun onFailure(call: Call<List<MenuOrder>>, t: Throwable) {
                        // 요청이 실패한 경우 처리할 내용
                    }
                })
            }
        }
    }
}