package com.example.endp

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.annotation.UiThread
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.example.endp.databinding.ActivityMainBinding
import com.example.endp.databinding.ActivityMapBinding
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraAnimation
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapFragment
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.util.FusedLocationSource
import com.naver.maps.map.util.MarkerIcons
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Locale

class MapActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mapView: MapView
    private val LOCATION_PERMISSTION_REQUEST_CODE: Int = 1000

    lateinit var binding : ActivityMapBinding


    private val marker = Marker()
    private val marker1 = Marker()
    private val marker2 = Marker()



    //위치 정보 권한들을 배열로 설정
    private val PERMISSIONS = arrayOf(
        android.Manifest.permission.ACCESS_FINE_LOCATION,
        android.Manifest.permission.ACCESS_COARSE_LOCATION
    )



    //NaverMap 사용 위해 setting.gradle에 추가
    private var naverMap: NaverMap ?= null

    //위치 추적 기능 (내장)
    private lateinit var locationSource: FusedLocationSource
    override fun onCreate(savedInstanceState: Bundle?) {

        var str1: String? = null
        var str2: String? = null
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mapView = findViewById(R.id.map)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        locationSource = FusedLocationSource(this, LOCATION_PERMISSTION_REQUEST_CODE)


//        val toolbar: Toolbar = findViewById(R.id.toolbar)
//        setSupportActionBar(toolbar)
//        supportActionBar?.setDisplayShowTitleEnabled(false)
//
//        fun setSpinnerAdapter(arrayResId: Int) {
//            val spinner2: Spinner = toolbar.findViewById(R.id.spinner2)
//            val adapter2 = ArrayAdapter.createFromResource(this,
//                arrayResId, android.R.layout.simple_spinner_item)
//            adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//            spinner2.adapter = adapter2
////            spinner2.setSelection(0) // 선택 초기화
//        }
//        val spinner: Spinner = toolbar.findViewById(R.id.spinner1)
//        val adapter = ArrayAdapter.createFromResource(this,
//            R.array.spinner_items1, android.R.layout.simple_spinner_item)
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//        spinner.adapter = adapter
//        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//                // 스피너 아이템이 선택되었을 때
//                val selectedItem = parent?.getItemAtPosition(position).toString()
//                when (selectedItem) {
//                    "지역 선택" -> setSpinnerAdapter(R.array.미선택)
//                    "강원도" -> setSpinnerAdapter(R.array.강원도)
//                    "경기도" -> setSpinnerAdapter(R.array.경기도)
//                    "경상남도" -> setSpinnerAdapter(R.array.경상남도)
//                    "경상북도" -> setSpinnerAdapter(R.array.경상북도)
//                    "광주광역시" -> setSpinnerAdapter(R.array.광주광역시)
//                    "대구광역시" -> setSpinnerAdapter(R.array.대구광역시)
//                    "대전광역시" -> setSpinnerAdapter(R.array.대전광역시)
//                    "부산광역시" -> setSpinnerAdapter(R.array.부산광역시)
//                    "서울특별시" -> setSpinnerAdapter(R.array.서울특별시)
//                    "세종특별자치시" -> setSpinnerAdapter(R.array.세종특별자치시)
//                    "울산광역시" -> setSpinnerAdapter(R.array.울산광역시)
//                    "인천광역시" -> setSpinnerAdapter(R.array.인천광역시)
//                    "전라남도" -> setSpinnerAdapter(R.array.전라남도)
//                    "전라북도" -> setSpinnerAdapter(R.array.전라북도)
//                    "제주특별자치도" -> setSpinnerAdapter(R.array.제주특별자치도)
//                    "충청남도" -> setSpinnerAdapter(R.array.충청남도)
//                    "충청북도" -> setSpinnerAdapter(R.array.충청북도)
//                }
//            }
//            override fun onNothingSelected(parent: AdapterView<*>?) {
//                // 아무 것도 선택되지 않았을 때
//            }
//
//        }
//        binding.spinner1.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//                val selectedItem = parent?.getItemAtPosition(position).toString()
//
//                // 스피너1 아이템이 선택되었을 때
//                if (selectedItem == "지역 선택") {
//                    setSpinnerAdapter(R.array.미선택)
//                    binding.spinner2.isEnabled = false // spinner2 비활성화
//                } else {
//                    // 다른 지역이 선택되었을 때
//
//                    binding.spinner2.isEnabled = true // spinner2 활성화
//                }
//            }
//
//            override fun onNothingSelected(parent: AdapterView<*>?) {
//                // 아무 것도 선택되지 않았을 때
//            }
//        }
////
//        binding.spinner2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//                val selectedItem = parent?.getItemAtPosition(position).toString()
//                if (selectedItem != "미선택") {
//                    str2 = selectedItem
//                } else {
//                    str2 = null
//                }
//            }
//
//            override fun onNothingSelected(parent: AdapterView<*>?) {
//                str2 = null
//            }
//        }

//
//        binding.search.setOnClickListener {
//
//            if (str1 == "서울특별시" && str2 == "서대문구") {
//                marker1.captionText = "서울특별시 서대문구 신촌점"
//
//                val marker = Marker()
//                val atLocation = LatLng(37.5547, 126.9372)
//                marker.position = atLocation
//                marker.map = naverMap
//
//                // 마커 위치로 카메라 이동
//                val cameraUpdate = CameraUpdate.scrollTo(atLocation)
//                    .animate(CameraAnimation.Easing)
//
//                naverMap?.moveCamera(cameraUpdate)
//
//            } else if(str1 == "서울특별시" && str2 == "강남구") {
//                marker2.captionText = "서울특별시 강남구 강남점"
//
//                val marker = Marker()
//                val atLocation = LatLng(37.498021, 127.028545)
//                marker.position = atLocation
//                marker.map = naverMap
//
//                // 마커 위치로 카메라 이동
//                val cameraUpdate = CameraUpdate.scrollTo(atLocation)
//                    .animate(CameraAnimation.Easing)
//
//                naverMap?.moveCamera(cameraUpdate)
//
//            }else{
//
//            }
//        }
        binding.myLocation.setOnClickListener {

        }

    }


    override fun onMapReady(@NonNull naverMap: NaverMap) {
        this.naverMap = naverMap
        naverMap.locationSource = locationSource
        naverMap.locationTrackingMode = LocationTrackingMode.Follow

        // 현재 위치 마커
        marker.position = LatLng(37.6281, 127.0905)
        marker.map = naverMap
        marker.icon = MarkerIcons.BLACK
        marker.iconTintColor = Color.RED // 현재위치 마커 빨간색으로

        // 장소 리스트 마커
        marker1.position = LatLng(37.5547, 126.9372)
        marker1.map = naverMap
        marker1.captionText = "서울특별시 서대문구 신촌점"
        marker2.position = LatLng(37.498021, 127.028545)
        marker2.map = naverMap // 고씨네
        marker2.captionText = "서울특별시 강남구 강남점"
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }



}





