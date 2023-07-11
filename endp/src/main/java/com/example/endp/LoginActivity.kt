package com.example.endp

import android.content.Intent
import android.os.Bundle
import android.service.autofill.UserData
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import com.example.endp.databinding.ActivityLoginBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoginActivity : AppCompatActivity() {
    private lateinit var edt1: EditText
    private lateinit var edt2: EditText
    private lateinit var btn1: Button
    private lateinit var apiService: ApiService
    private lateinit var userList: List<User>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        edt1 = findViewById(R.id.edt1)
        edt2 = findViewById(R.id.edt2)
        btn1 = findViewById(R.id.btn1)

        // Retrofit 인스턴스 생성
        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.100.57:8090")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        // API 서비스 인터페이스 생성
        apiService = retrofit.create(ApiService::class.java)

        btn1.setOnClickListener {
            val username = edt1.text.toString()
            val password = edt2.text.toString()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                // Perform API call on a background thread using coroutines
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val response = apiService.getUserAll().execute()
                        if (response.isSuccessful) {
                            withContext(Dispatchers.Main) {
                                val userList: List<User> = response.body() ?: emptyList()
                                handleLogin(username, password, userList)
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
            } else {
                Toast.makeText(applicationContext, "ID와 Password를 입력하세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun handleLogin(username: String, password: String, userList: List<User>) {
        // 사용자 목록에서 인증 확인
        val user = userList.find { it.username == username && it.password == password }
        if (user != null) {
            // 로그인 성공
            Toast.makeText(applicationContext, "로그인 성공", Toast.LENGTH_SHORT).show()
            // 로그인 후 처리할 작업 수행
            navigateToMain()
        } else {
            // 로그인 실패
            Toast.makeText(applicationContext, "로그인 실패", Toast.LENGTH_SHORT).show()
        }
    }

    private fun navigateToMain() {
        val intent = Intent(this, MainActivity2::class.java)
        startActivity(intent)
        finish() // Optional: Finish the LoginActivity to prevent going back
    }
}





