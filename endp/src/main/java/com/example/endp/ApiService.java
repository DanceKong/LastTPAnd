package com.example.endp;



import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {
    @POST("test/login")
//    Call<User> login( @Query("username") String username,
//                      @Query("password") String password);
    Call<User> login( @Body LoginRequest loginRequest);
}
