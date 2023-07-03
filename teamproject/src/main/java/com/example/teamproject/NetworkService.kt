package com.example.teamproject

import com.example.teamproject.model.ModelList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface NetworkService {
//    @GET("getParmacyListInfoInqire")
//    fun getList(
//        @Query("serviceKey") serviceKey: String,
//        @Query("Q0") q0: String,
//        @Query("Q1") q1: String,
//        @Query("Q2") q2: Int,
//        @Query("numOfRows") numOfRows: Int
//    ): Call<ModelList>

    @GET("getParmacyListInfoInqire")
    fun getList(
        @Query("serviceKey") serviceKey: String,
        @Query("Q0") q0: String,
        @Query("Q1") q1: String,
        @Query("numOfRows") numOfRows: Int
    ): Call<ModelList>

//    @GET("getParmacyBasisList")
//    fun getList(
//        @Query("serviceKey") serviceKey: String,
//        @Query("sidoCd") sidoCd: String,
//        @Query("sgguCd") sgguCd: String,
//        @Query("numOfRows") numOfRows: Int
//    ):Call<ModelList>

//    @GET("getParmacyBasisList")
//    fun getList(
//        @Query("serviceKey") serviceKey: String,
//        @Query("pageNo") pageNo: Int,
//        @Query("numOfRows") numOfRows: Int,
//        @Query("emdongNm") emdongNm: String
//    ):Call<ModelList>
}