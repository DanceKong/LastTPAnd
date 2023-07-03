package com.example.teamproject.model

import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml

@Xml(name = "response")
data class ModelList(
    @Element(name = "body")
    val body: Body?,
    @Element(name = "header")
    val header: Header?

)

@Xml(name = "header")
data class Header (
    @PropertyElement(name = "resultCode")
    val resultCode: Int,
    @PropertyElement(name = "resultMsg")
    val resultMsg: String
)

@Xml(name = "body")
data class Body(
    @Element(name="items")
    val items: Items,
    @PropertyElement(name="numOfRows")
    val numOfRows: Int,
    @PropertyElement(name="pageNo")
    val pageNo: Int,
    @PropertyElement(name="totalCount")
    val totalCount: Int
)

@Xml(name = "items")
data class Items(
    @Element(name = "item")
    val item:List<Item>
)
@Xml
data class Item(
    @PropertyElement(name = "dutyAddr")
    var dutyAddr: String? = null,

    @PropertyElement(name = "dutyMapimg")
    var dutyMapimg: String? = null,

    @PropertyElement(name = "dutyName")
    var dutyName: String? = null,

    @PropertyElement(name = "dutyTel1")
    var dutyTel1: String? = null,

    @PropertyElement(name = "dutyTime1c")
    var dutyTime1c: String? = null,

    @PropertyElement(name = "dutyTime1s")
    var dutyTime1s: String? = null,

    @PropertyElement(name = "dutyTime2c")
    var dutyTime2c: String? = null,

    @PropertyElement(name = "dutyTime2s")
    var dutyTime2s: String? = null,

    @PropertyElement(name = "dutyTime3c")
    var dutyTime3c: String? = null,

    @PropertyElement(name = "dutyTime3s")
    var dutyTime3s: String? = null,

    @PropertyElement(name = "dutyTime4c")
    var dutyTime4c: String? = null,

    @PropertyElement(name = "dutyTime4s")
    var dutyTime4s: String? = null,

    @PropertyElement(name = "dutyTime5c")
    var dutyTime5c: String? = null,

    @PropertyElement(name = "dutyTime5s")
    var dutyTime5s: String? = null,

    @PropertyElement(name = "dutyTime6c")
    var dutyTime6c: String? = null,

    @PropertyElement(name = "dutyTime6s")
    var dutyTime6s: String? = null,

    @PropertyElement(name = "dutyTime7c")
    var dutyTime7c: String? = null,

    @PropertyElement(name = "dutyTime7s")
    var dutyTime7s: String? = null,

    @PropertyElement(name = "wgs84Lat")
    var wgs84Lat: Double? = null,

    @PropertyElement(name = "wgs84Lon")
    var wgs84Lon: Double? = null
)

//@Xml
//data class Item(
//    @PropertyElement(name="yadmNm")
//    var yadmNm:String ?=null,
//    @PropertyElement(name="addr")
//    var addr:String ?= null,
//    @PropertyElement(name="telno")
//    var telno:String ?= null,
//    @PropertyElement(name="XPos")
//    var XPos: Double ?= null,
//    @PropertyElement(name = "YPos")
//    var YPos: Double ?= null
//
//)