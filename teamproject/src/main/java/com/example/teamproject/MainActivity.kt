package com.example.teamproject

import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.UiThread
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar

import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService

import com.example.teamproject.databinding.ActivityMainBinding
import com.example.teamproject.model.ModelList
import com.google.android.material.navigation.NavigationView
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.CircleOverlay
import com.naver.maps.map.overlay.LocationOverlay
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.util.FusedLocationSource
import com.naver.maps.map.util.MarkerIcons
import com.tickaroo.tikxml.TikXml
import com.tickaroo.tikxml.retrofit.TikXmlConverterFactory

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.util.Locale


class MainActivity : AppCompatActivity(), OnMapReadyCallback {  //MapAPI와 상호작용 하기 위한 OnMapReadyCallback
    //다른 권한 요청에 대한 요청 코드와 구별하기 위해 사용
    private val LOCATION_PERMISSION_REQUEST_CODE = 5000  //권한 요청 및 결과 처리 시 일관된 코드를 유지하고 권한 요청의 흐름을 추적하는 데 도움

    lateinit var binding : ActivityMainBinding
    // 현재위치 주소정보를 저장하기 위한 리스트 생성
    private var myLocatinlList: MutableList<String> = mutableListOf()

    //위치 정보 권한들을 배열로 설정
    private val PERMISSIONS = arrayOf(
        android.Manifest.permission.ACCESS_FINE_LOCATION,
        android.Manifest.permission.ACCESS_COARSE_LOCATION
    )



    //NaverMap 사용 위해 setting.gradle에 추가
    private var naverMap: NaverMap ?= null

    //위치 추적 기능 (내장)
    private lateinit var locationSource: FusedLocationSource

    // Base_URL 과 serviceKey(인증키) 선언
    val BASE_URL = "https://apis.data.go.kr/B552657/ErmctInsttInfoInqireService/"
    val serviceKey =
        "CgiQOjwmdhqEP9T475acdKF6v/mXnJA2g08aKnDJwvaUGA6PeK++z2GHGjwt/PvN4OkeAQIXqSCAIMEwWCv9cA=="

    private val markers = ArrayList<Marker>()

    //레트로핏 사용을 위한 설정
    val retrofit: Retrofit
        get() = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(
                TikXmlConverterFactory.create(          //xml 자료로 파싱하기 위하여...
                    //kapt 'com.tickaroo.tikxml:processor:0.8.13' Module Gradle에 추가
                    // 추가 화면 참조

                    TikXml.Builder().exceptionOnUnreadXml(false).build()
                )
            )
            .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //CardView 숨김
        binding.cardView.visibility = View.GONE
        initMapView()

        //위치 권한 확인 후 권한이 없는 경우 권한 요청하는 부분, 권한 있는 경우 initMapView() 호출
        if (!hasPermission()) {
            initMapView()
        } else {
           initializeMap()
        }

        //툴바 설정
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)



        // 스피너 어댑터 동적 설정 함수
        //스피너란? 사용자에게 선택할 수 있는 목록을 표시하는 드롭다운 스타일의 레이아웃

        fun setSpinnerAdapter(arrayResId: Int) {
            val spinner2: Spinner = toolbar.findViewById(R.id.spinner2)
            val adapter2 = ArrayAdapter.createFromResource(this,
                arrayResId, android.R.layout.simple_spinner_item)
            adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner2.adapter = adapter2
            spinner2.setSelection(0) // 선택 초기화
        }
        val spinner: Spinner = toolbar.findViewById(R.id.spinner1)
        val adapter = ArrayAdapter.createFromResource(this,
            R.array.spinner_items1, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // 스피너 아이템이 선택되었을 때
                val selectedItem = parent?.getItemAtPosition(position).toString()
                when (selectedItem) {
                    "지역 선택" -> setSpinnerAdapter(R.array.미선택)
                    "강원도" -> setSpinnerAdapter(R.array.강원도)
                    "경기도" -> setSpinnerAdapter(R.array.경기도)
                    "경상남도" -> setSpinnerAdapter(R.array.경상남도)
                    "경상북도" -> setSpinnerAdapter(R.array.경상북도)
                    "광주광역시" -> setSpinnerAdapter(R.array.광주광역시)
                    "대구광역시" -> setSpinnerAdapter(R.array.대구광역시)
                    "대전광역시" -> setSpinnerAdapter(R.array.대전광역시)
                    "부산광역시" -> setSpinnerAdapter(R.array.부산광역시)
                    "서울특별시" -> setSpinnerAdapter(R.array.서울특별시)
                    "세종특별자치시" -> setSpinnerAdapter(R.array.세종특별자치시)
                    "울산광역시" -> setSpinnerAdapter(R.array.울산광역시)
                    "인천광역시" -> setSpinnerAdapter(R.array.인천광역시)
                    "전라남도" -> setSpinnerAdapter(R.array.전라남도)
                    "전라북도" -> setSpinnerAdapter(R.array.전라북도)
                    "제주특별자치도" -> setSpinnerAdapter(R.array.제주특별자치도)
                    "충청남도" -> setSpinnerAdapter(R.array.충청남도)
                    "충청북도" -> setSpinnerAdapter(R.array.충청북도)
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                // 아무 것도 선택되지 않았을 때
            }
        }
        binding.search.setOnClickListener {
            var str1 = binding.spinner1.selectedItem.toString()
            var str2 = binding.spinner2.selectedItem.toString()
            performSearch(str1, str2)
        }
        binding.myLocation.setOnClickListener {
            myLoc()
        }
    }

    //맵 뷰 초기화
    private fun initMapView() {
        //id 존재시 MapFragment 추가
        val fm = supportFragmentManager
        val mapFragment = fm.findFragmentById(R.id.map) as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.map, it).commit()
            }
        //getMapAsync 사용으로 onMapReady가 자동으로 호출
        mapFragment.getMapAsync(this)
        // 내장 위치 추적 기능
        locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)
    }

    //위치 권한 요청
    private fun hasPermission(): Boolean {
        for (permission in PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, permission)
                != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }
        return true
    }

    @UiThread
    override fun onMapReady(naverMap: NaverMap){
        this.naverMap = naverMap
        initializeMap()
    }

    private fun initializeMap(){
        // 내 위치 불러오기 버튼 활성화

        //위치 정보
        naverMap?.locationSource = locationSource
        //버튼 활성화
        naverMap?.uiSettings?.isLocationButtonEnabled = true
        //위치 추적
        naverMap?.locationTrackingMode = LocationTrackingMode.Follow

    }


    private fun myMarkerLoc(str1:String, str2:String) {
        val markers = ArrayList<Marker>()
        //Retrofit을 사용하여 네트워크 서비스와 연결하고, API 요청을 생성 ,ListCall에 할당
        val networkService: NetworkService = retrofit.create(NetworkService::class.java)
        val ListCall = networkService.getList(serviceKey,str1,str2,100)
        do {
            ListCall.enqueue(object : Callback<ModelList> {
                override fun onResponse(call: Call<ModelList>, response: Response<ModelList>) {
                    val infoList = response.body()
                    if(infoList == null) Toast.makeText(this@MainActivity,"데이터 못받음",Toast.LENGTH_SHORT).show()
                    infoList?.body?.items?.item?.forEach { item ->
                        val wgs84Lat = item.wgs84Lat
                        val wgs84Lon = item.wgs84Lon
                        val atLocation = LatLng(wgs84Lat!!, wgs84Lon!!)
                        val marker = Marker()
                        //  값 전달 받고, 분할 처리 O
                        marker.position = atLocation
                        marker.icon = MarkerIcons.BLACK
                        marker.iconTintColor = Color.RED
                        markers.add(marker)
                        marker.map = naverMap
                        // 마커 클릭 리스너 설정
                        marker.setOnClickListener { marker ->
                            //마커 클릭시 CardView 표시
                            binding.cardView.visibility = View.VISIBLE
                            var name = findViewById<TextView>(R.id.name)
                            var addr = findViewById<TextView>(R.id.addr)
                            var tel = findViewById<TextView>(R.id.tel)
                            var mon = findViewById<TextView>(R.id.mon)
                            var tue = findViewById<TextView>(R.id.tue)
                            var wed = findViewById<TextView>(R.id.wed)
                            var thur = findViewById<TextView>(R.id.thur)
                            var fri = findViewById<TextView>(R.id.fri)
                            var sat = findViewById<TextView>(R.id.sat)
                            var sun = findViewById<TextView>(R.id.sun)
                            var arr = marker.tag.toString().split("/") // 마커에 붙인 태그
                            name.text = item.dutyName
                            addr.text = item.dutyAddr
                            tel.text = item.dutyTel1
                            mon.text =
                                stBuffer("${item.dutyTime1s} ~ ${item.dutyTime1c}")
                            tue.text =
                                stBuffer("${item.dutyTime2s} ~ ${item.dutyTime2c}")
                            wed.text =
                                stBuffer("${item.dutyTime3s} ~ ${item.dutyTime3c}")
                            thur.text =
                                stBuffer("${item.dutyTime4s} ~ ${item.dutyTime4c}")
                            fri.text =
                                stBuffer("${item.dutyTime5s} ~ ${item.dutyTime5c}")
                            if (item.dutyTime6s.equals("null") || item.dutyTime6s == null) {
                                sat.text = "휴무"
                            } else {
                                sat.text =
                                    stBuffer("${item.dutyTime6s} ~ ${item.dutyTime6c}")
                            }
                            if (item.dutyTime7s.equals("null") || item.dutyTime7s == null) {
                                sun.text = "휴무"
                            } else {
                                sun.text =
                                    stBuffer("${item.dutyTime7s} ~ ${item.dutyTime7c}")
                            }
                            true
                        }
                        //Map 클릭 시 CardView 숨김 이벤트
                        naverMap?.setOnMapClickListener { pointF, latLng ->
                            binding.cardView.visibility = View.GONE
                            // 현재 포커스가 있는 View를 가져옵니다.
                            val focusedView = currentFocus
                            // InputMethodManager를 가져옵니다.
                            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                            // 포커스된 View가 있고, 키보드가 열려있는 경우에만 키보드를 숨깁니다.
                            focusedView?.let {
                                imm.hideSoftInputFromWindow(it.windowToken, 0)
                            }
                        }
                    }
                }
                override fun onFailure(call: Call<ModelList>, t: Throwable) {
                    call.cancel()
                }
            })
        }while (ListCall == null)
        markers.forEach { marker ->
            marker.map = null
        }
        markers.clear()
    }

    //검색결과에 따른 API를 받기 위한 설정
    private fun performSearch(str1: String, str2: String) {
        val networkService: NetworkService = retrofit.create(NetworkService::class.java)
        val numOfRows = 1000 // 한 페이지에 가져올 항목의 개수
        var Page = 1 // 현재 페이지
        var totalItems = 0 // 전체 항목 개수

        // totalCount를 가져오는 첫 번째 API 호출
        networkService?.getList(serviceKey, str1, str2, numOfRows)?.enqueue(object : Callback<ModelList> {
            override fun onResponse(call: Call<ModelList>, response: Response<ModelList>) {
                val infoList = response.body()
                totalItems = infoList?.body?.totalCount ?: 0
                val totalPages = (totalItems + numOfRows - 1) / numOfRows // 전체 페이지 개수

                // 각 페이지에 대한 API 호출을 순차적으로 진행
                for (page in 1..totalPages) {
                    networkService?.getList(serviceKey, str1, str2, numOfRows)
                        ?.enqueue(object : Callback<ModelList> {
                            override fun onResponse(
                                call: Call<ModelList>,
                                response: Response<ModelList>
                            ) {
                                // itemList을 처리하는 코드 작성
                                markers.forEach { marker ->
                                    marker.map = null
                                }
                                markers.clear()

                                val infoList = response.body()
                                infoList?.body?.items?.item?.forEach { item ->
                                    val wgs84Lat = item.wgs84Lat        //위도
                                    val wgs84Lon = item.wgs84Lon        //경도

                                    if(wgs84Lat == null || wgs84Lat.equals("null")){

                                    }else{
                                        val atLocation = LatLng(wgs84Lat!!, wgs84Lon!!)
                                        val marker = Marker()

                                        //마커 위치 설정
                                        marker.position = atLocation
                                        marker.map = naverMap

                                        val cameraUpdate = CameraUpdate.scrollTo(atLocation)
                                        naverMap?.moveCamera(cameraUpdate)


                                        //마커 색 분류
                                        marker.icon = MarkerIcons.BLACK
                                        marker.iconTintColor = Color.RED

                                        // 마커 추가
                                        markers.add(marker)

                                        marker.setOnClickListener { marker ->
                                            //CardView를 사용해 데이터 출력을 위하여
                                            //implementation("androidx.cardview:cardview:1.0.0") Module Gradle에 추가
                                            //마커 클릭시 CardView 표시
                                            binding.cardView.visibility = View.VISIBLE
                                            var name = findViewById<TextView>(R.id.name)
                                            var addr = findViewById<TextView>(R.id.addr)
                                            var tel = findViewById<TextView>(R.id.tel)
                                            var mon = findViewById<TextView>(R.id.mon)
                                            var tue = findViewById<TextView>(R.id.tue)
                                            var wed = findViewById<TextView>(R.id.wed)
                                            var thur = findViewById<TextView>(R.id.thur)
                                            var fri = findViewById<TextView>(R.id.fri)
                                            var sat = findViewById<TextView>(R.id.sat)
                                            var sun = findViewById<TextView>(R.id.sun)
                                            name.text = item.dutyName
                                            addr.text = item.dutyAddr
                                            tel.text = item.dutyTel1
                                            mon.text =
                                                stBuffer("${item.dutyTime1s} ~ ${item.dutyTime1c}")
                                            tue.text =
                                                stBuffer("${item.dutyTime2s} ~ ${item.dutyTime2c}")
                                            wed.text =
                                                stBuffer("${item.dutyTime3s} ~ ${item.dutyTime3c}")
                                            thur.text =
                                                stBuffer("${item.dutyTime4s} ~ ${item.dutyTime4c}")
                                            fri.text =
                                                stBuffer("${item.dutyTime5s} ~ ${item.dutyTime5c}")
                                            if (item.dutyTime6s.equals("null") || item.dutyTime6s == null) {
                                                sat.text = "휴무"
                                            } else {
                                                sat.text =
                                                    stBuffer("${item.dutyTime6s} ~ ${item.dutyTime6c}")
                                            }
                                            if (item.dutyTime7s.equals("null") || item.dutyTime7s == null) {
                                                sun.text = "휴무"
                                            } else {
                                                sun.text =
                                                    stBuffer("${item.dutyTime7s} ~ ${item.dutyTime7c}")
                                            }
                                            true
                                        }
                                    }

                                    //Map 클릭 시 CardView 숨김 이벤트
                                    naverMap?.setOnMapClickListener { pointF, latLng ->
                                        binding.cardView.visibility = View.GONE

                                        // 현재 포커스가 있는 View를 가져옵니다.
                                        val focusedView = currentFocus

                                        // InputMethodManager를 가져옵니다.
                                        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

                                        // 포커스된 View가 있고, 키보드가 열려있는 경우에만 키보드를 숨깁니다.
                                        focusedView?.let {
                                            imm.hideSoftInputFromWindow(it.windowToken, 0)
                                        }

                                    }
                                }
                            }

                            // 실패 처리 코드 작성
                            override fun onFailure(call: Call<ModelList>, t: Throwable) {

                            }
                        })
                }
            }
            // 실패 처리 코드 작성
            override fun onFailure(call: Call<ModelList>, t: Throwable) {

            }
        })
    }

    //영업 시간 표기를 위한 스트링버퍼 구분 (ex s 0900 c 1900)
    private fun stBuffer(text: String): String {
        val stringBuffer = StringBuffer()
        stringBuffer.append(text)
        stringBuffer.insert(2," : ")
        stringBuffer.insert(12, " : ")
        return stringBuffer.toString()
    }
    //내 위치 주소값에서 시/도 , 시/군/구 를  추출하기 위한 스트링버퍼
    private fun locationBuffer(text:String): List<String> {
        val stringBuffer = StringBuffer(text)
        var str = stringBuffer.split(" ")
        val list: List<String> = listOf(str.get(1),str.get(2))

        return list
    }

    // 내위치 위도 경도 값을 주소로 치환 후 MylocationLIst에 저장 및 마커 찍는 함수
    private fun myLoc() {
        val manager = getSystemService(LOCATION_SERVICE) as LocationManager

        if (ContextCompat.checkSelfPermission(
                this, android.Manifest.permission.ACCESS_FINE_LOCATION
            ) === PackageManager.PERMISSION_GRANTED
        ) {
            //위치 정보 얻기 getLastKnownLocation()
            val location: Location? = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER)

            location?.let {
                val latitude = location.latitude
                val longitude = location.longitude
                //위도와 경도에 대한 주소를 가져오는 역할 , 한국어로 가져오기 위한 Locale.KOREAN
                val geocoder = Geocoder(applicationContext, Locale.KOREAN)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    geocoder.getFromLocation(
                        latitude, longitude, 1
                    ) { address ->
                        if (address.size != 0) {
                            val list: List<String> =
                                locationBuffer(address[0].getAddressLine(0).toString())
                            myLocatinlList.add(list.get(0))
                            myLocatinlList.add(list.get(1))
                            myMarkerLoc(myLocatinlList.get(0), myLocatinlList.get(1))
                        }
                    }
                } else {  //퍼미션 33미만인 경우...
                    val addresses = geocoder.getFromLocation(latitude, longitude, 1)
                    if (addresses != null) {
                        val list: List<String> =
                            locationBuffer(addresses[0].getAddressLine(0).toString())
                        myLocatinlList.add(list.get(0))
                        myLocatinlList.add(list.get(1))
                        myMarkerLoc(myLocatinlList.get(0), myLocatinlList.get(1))
                    }

                }
            }
        } else {
            Toast.makeText(this, "권한 허용이 필요합니다. 앱 설정에서 권한을 허용해주세요.", Toast.LENGTH_SHORT).show()
        }
    }
}