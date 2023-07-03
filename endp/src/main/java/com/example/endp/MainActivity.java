package com.example.endp;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;



public class MainActivity extends AppCompatActivity {
    private ApiService apiService;


    private TextView textViewResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewResult = findViewById(R.id.text_view_result);


        // Retrofit 인스턴스 생성
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://localhost:8090/") // REST API 서버의 기본 URL
                .addConverterFactory(GsonConverterFactory.create()) // JSON 데이터를 자바 객체로 변환하기 위해 Gson 컨버터 사용
                .build();

        // API 서비스 인터페이스 생성
        apiService = retrofit.create(ApiService.class);

        // 서버로부터 데이터 가져오기
        Call<List<User>> call = apiService.getUsers();
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful()) {

                    textViewResult.setText("code: "+response.code());
                    List<User> userList = response.body();
                    if (userList != null && !userList.isEmpty()) {
                        User user = userList.get(0);
                        String username = user.getUsername();
                        String password = user.getPassword();
                        String name = user.getName();



                        Log.d("User", "Username: " + user.getUsername());
                        Log.d("User", "Name: " + user.getPassword());
                        textViewResult.setText("Username: " + username);

                    }
                }
            }


            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Failed to fetch user data", Toast.LENGTH_SHORT).show();
            }
        });


    }

}