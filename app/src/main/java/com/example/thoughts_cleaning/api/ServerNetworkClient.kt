package com.example.thoughts_cleaning.api

import android.R.attr.timeZone
import android.os.Build
import android.util.Log
import com.example.thoughts_cleaning.MainApplication
import com.example.thoughts_cleaning.api.base.TrustFactory
import com.example.thoughts_cleaning.common.util.getLanguage
import com.example.thoughts_cleaning.common.util.getVersionName
import com.example.thoughts_cleaning.common.vm.BaseViewModel
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.IllegalArgumentException
import com.example.thoughts_cleaning.common.Constants
import com.example.thoughts_cleaning.util.TimeUtils

object ServerNetworkClient:Interceptor {

    // API 서버의 기본 URL (반드시 '/'로 끝나야 합니다)
//    private const val BASE_URL = "https://api.example.com/"
    private const val BASE_URL = "http://thoughtclean.dnx.kr:8910/"

    private var touchCareApi = getRetrofit(getHttpClient()).create(ServerNetworkApi::class.java)



    fun init() {
        touchCareApi = getRetrofit(getHttpClient()).create(ServerNetworkApi::class.java)
    }

    fun getClient(): ServerNetworkApi {
        return touchCareApi
    }


    // HttpLoggingInterceptor를 추가하여 네트워크 통신 로그를 확인할 수 있습니다.
    private val okHttpClient = OkHttpClient.Builder()
//        .addInterceptor(HttpLoggingInterceptor().apply {
//            level = HttpLoggingInterceptor.Level.BODY // 통신 내용 전체 로깅
//        })
        .connectTimeout(10, TimeUnit.SECONDS) // 연결 시간 초과
        .readTimeout(10, TimeUnit.SECONDS)    // 읽기 시간 초과
        .writeTimeout(10, TimeUnit.SECONDS)   // 쓰기 시간 초과
        .build()

    // Retrofit 인스턴스 초기화 (Lazy initialization)
//    private val retrofit: Retrofit by lazy {
//        Retrofit.Builder()
//            .baseUrl(BASE_URL)
//            // JSON 응답을 Data Class로 자동 변환하기 위해 Gson Converter 사용
//            .addConverterFactory(GsonConverterFactory.create())
//            .client(okHttpClient)
//            .build()
//    }

//    // ServerNetworkApi 인터페이스의 구현체를 얻는 메서드
//    val apiService: ServerNetworkApi by lazy {
//        retrofit.create(ServerNetworkApi::class.java)
//    }

    private fun getRetrofit(client: OkHttpClient): Retrofit {
        Prefs.baseUrl = BASE_URL

        try {
            return Retrofit.Builder()
                .baseUrl(Prefs.baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
        }catch (e: IllegalArgumentException) {
//            val baseUrl = "http://34.69.78.123"
            Prefs.baseUrl = BASE_URL

            return Retrofit.Builder()
                .baseUrl(Prefs.baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
        }
    }

    private fun getHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.HEADERS
        logging.level = HttpLoggingInterceptor.Level.BODY

        val builder = OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .protocols(listOf(Protocol.HTTP_1_1))
            .addInterceptor(logging)
            .addInterceptor(this)

        //android 6.0 이하 버전 letsencrypt ssl 문제로 적용
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            val (socketFactory, trustManager) = TrustFactory.getTrustFactoryManager(MainApplication.instance.applicationContext)
            builder.sslSocketFactory(socketFactory, trustManager)
        }

        return builder.build()
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val url: String = chain.request().url.toString()
        val language = MainApplication.instance.getLanguage()
        val version = MainApplication.instance.getVersionName()
//        val timeZone = TimeUtils.getTimeZone()
        val timeZoneString = if (timeZone > 0) "+$timeZone" else if (timeZone < 0) "$timeZone" else "0"

        val builder: Request.Builder = chain.request()
            .newBuilder()
            .removeHeader("User-Agent")
            .addHeader("Content-Type", "application/json")
            .addHeader("User-Agent", "${Constants.USER_AGENT} $language $timeZoneString $version")
            .addHeader(
                if (isAddedUserNow(Prefs.baseUrl, url)) "Dnx-Test-Now" else "Dnx-User-Now",
                TimeUtils.getCurrentDate("")
            )

        Log.e(BaseViewModel.TAG, "intercept1: ", )
        Log.e(BaseViewModel.TAG, "intercept1: ", )
        Log.e(BaseViewModel.TAG, "intercept1: ", )
        Log.e(BaseViewModel.TAG, "intercept1: ${Prefs.accessToken}", )
//        Log.d("sessionKey_check", Prefs.accessToken)


//        if (isNeedSessionKey(Prefs.baseUrl, url)) {
            val sessionKey: String? = Prefs.accessToken
            Log.e(BaseViewModel.TAG, "intercept: 2", )


            if(isHaveSessionKey(sessionKey)) {
                Log.e(BaseViewModel.TAG, "intercept: 3", )

                val authorizationValue = String.format("Bearer %s", sessionKey)
                builder.addHeader("Authorization", authorizationValue)
            }
//        }
        return chain.proceed(builder.build())
    }

//    private fun isNeedSessionKey(baseUrl: String, url: String): Boolean {
//        when {
//            url.contains("/xouchcare/user/u/token") -> {
//                return false
//            }
//            url.contains("/xouchcare/user/u/term/list") -> {
//                return false
//            }
//            url.contains("/xouchcare/sms/cert/request") -> {
//                return false
//            }
//            url.contains("/xouchcare/sms/cert/verify") -> {
//                return false
//            }
//            url.contains("/xouchcare/user/u/check_login_id") -> {
//                return false
//            }
//            else -> return url.contains(baseUrl)
//        }
//    }

    private fun isHaveSessionKey(sessionKey: String?): Boolean {
        if (sessionKey!!.isNotEmpty()) {
            return true
        }

        Prefs.initUserInfo()
//        EventBus.post(Constants.INSTANCE_STOP_SERVICE)
        return false
    }

    private fun isAddedUserNow(baseUrl: String?, url: String): Boolean {
        return url.startsWith("$baseUrl/xouchcare/cb3")
                && url.endsWith("touch")
                && !url.contains("/p/")
    }

}