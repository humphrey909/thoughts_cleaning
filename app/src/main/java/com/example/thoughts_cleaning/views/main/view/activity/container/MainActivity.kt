package com.example.thoughts_cleaning.views.main.view.activity.container

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.thoughts_cleaning.views.main.vm.activity.container.MainActivityViewModel
import com.example.thoughts_cleaning.R
import com.example.thoughts_cleaning.databinding.ActivityMainBinding
import com.example.thoughts_cleaning.util.GameView
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.thoughts_cleaning.views.start.view.activity.container.StartActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainActivityViewModel by viewModels()

    private var isUserLoggedIn = false

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()

//        splashScreen.setKeepOnScreenCondition {
//            // isUserLoggedIn 상태가 결정될 때까지 true를 반환하여 스플래시 화면을 유지
//            !isUserLoggedIn
//        }

        // 3. (비동기) 로그인 상태 확인 로직 시작
        // 백그라운드에서 빠르게 로그인 여부 또는 초기 데이터를 로딩합니다.
        checkUserLoginStatus()

        // 4. 스플래시 화면 종료 리스너 (선택 사항)
        // 스플래시 화면이 사라진 직후에 다음 Activity로 이동하는 로직을 추가합니다.
        splashScreen.setOnExitAnimationListener {
            // 로그인 상태에 따라 다음 화면 결정
            if (isUserLoggedIn) {
                // 로그인 O: 메인 화면 (예: HomeActivity)
//                Intent(this, MainActivity::class.java)
            } else {
                // 로그인 X: 로그인 화면 (예: LoginActivity)
                val nextActivity = Intent(this, StartActivity::class.java)
                startActivity(nextActivity)
                finish() // MainActivity(Splash 화면 역할) 종료
            }
            // 애니메이션 제거 (필수)
            it.remove()
        }


        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)

        // 3. 바인딩 객체의 root 뷰를 Activity의 컨텐츠 뷰로 설정
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_container) as? NavHostFragment

        if (navHostFragment != null) {
            val navController = navHostFragment.navController

            // 4. BottomNavigationView와 NavController 연결
            // R.id.nav_view는 activity_main.xml에 있는 BottomNavigationView의 ID라고 가정합니다.
            binding.bottomNavInFragment.setupWithNavController(navController)
        } else {
            // 오류 처리: NavHostFragment를 찾을 수 없을 때의 로직
            // Log.e("MainActivity", "NavHostFragment not found!")
        }

        binding.bottomNavInFragment.itemIconTintList = null
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
//        if (::gameView.isInitialized) gameView.pause()
        super.onPause()
    }

    // (2) 로그인 상태를 체크하는 함수 (실제로는 비동기 작업 필요)
    private fun checkUserLoginStatus() {
        // TODO: 실제로는 SharedPreferences, DataStore, Firebase 등에서
        // 사용자 세션 또는 토큰의 유효성을 체크하는 비동기 작업이 들어갑니다.

        // 예시: 500ms(0.5초) 후 로그인 상태 결정
        // 실제 코드에서는 coroutine이나 Handler 등을 사용해 비동기 작업을 완료해야 합니다.
        // 예를 들어 Coroutine을 사용할 경우:
        /*
        lifecycleScope.launch {
            delay(500) // 로그인 체크에 필요한 최소 시간
            isUserLoggedIn = getLoginStatusFromDataStore() // 실제 로그인 상태 가져오기
        }
        */

        // **간단한 예시를 위해 500ms 후 isUserLoggedIn을 true로 설정**
        isUserLoggedIn = true
    }
}