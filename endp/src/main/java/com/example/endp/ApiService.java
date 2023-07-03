package com.example.endp;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    @GET("/user")
    Call<List<User>> getUsers();
}
