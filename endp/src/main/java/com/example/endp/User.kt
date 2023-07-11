package com.example.endp

import android.service.autofill.UserData
import android.view.Menu
import com.google.gson.Gson
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class User(
//    val createDate: String,
//    val modiDateTime: String,
//    val id: String,
    val username: String,
    val password: String,
//    val name: String,
//    val phone: String,
//    val email: String,
//    val address: String
)fun parseUserData(jsonData: String): List<UserData> {
    val gson = Gson()
    val dataList = gson.fromJson(jsonData, Array<UserData>::class.java)
    return dataList.toList()
}
data class Cart(
    @SerializedName("id")
    val id: Long?,
    @SerializedName("quantity")
    val quantity: Int,
    @SerializedName("user")
    val user: User?,
    @SerializedName("menu")
    val menu: Menu?
)

data class MenuOrder(
    @SerializedName("id")
    val id: Long?,
    @SerializedName("orderDate")
    val orderDate: String?,
    @SerializedName("quantity")
    val quantity: Int?,
    @SerializedName("menuId")
    val menuId: Menu?,
    @SerializedName("username")
    val username: String?,
    @SerializedName("orderNumber")
    val orderNumber: String?
)
