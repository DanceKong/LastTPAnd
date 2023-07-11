package com.example.endp

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitBuilder {
    object RetrofitBuilder {
        var api: ApiService
        init{
            val retrofit = Retrofit.Builder()
                .baseUrl("http://192.168.100.57:8090/test") // 요청 보내는 API 서버 url. /로 끝나야 함함
                .addConverterFactory(GsonConverterFactory.create()) // Gson을 역직렬화
                .build()
            api = retrofit.create(ApiService::class.java)
        }
    }
}