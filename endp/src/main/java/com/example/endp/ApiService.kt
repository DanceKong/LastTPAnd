package com.example.endp


import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
//    @GET("/test/select")
//    fun getAllUsers(): Call<List<User>>
//
//    @GET("/select/{username}")
//    fun getUserByUsername(@Path("username") username: String?): Call<User?>?
//    @FormUrlEncoded
//    @POST("/test/login")
//    fun loginUser(
//        @Field("username") username: String,
//        @Field("password") password: String
//    ): Call<String>
//
//    @GET("/select1")
//    fun getList(
//        @Query("username") username: String,
//        @Query("password") password: String,
//
//        ): Call<String>
    @GET("/test/select")
    fun getUserAll(): Call<List<User>>
    @FormUrlEncoded
    @POST("/test/select")
    fun getData(@FieldMap param: HashMap<String, @JvmSuppressWildcards Any>): Call<User>

    @GET("/test/select/{username}")
    fun getUserByUsernameAndPassword(
        @Query("username") username: String,
        @Query("password") password: String
    ): Call<List<User>>
    @POST("/test/logout")
    fun logout():Call<ResponseBody>

    @GET("/test/myorder")
    fun getMyOrder(): Call<List<MenuOrder>>


}

