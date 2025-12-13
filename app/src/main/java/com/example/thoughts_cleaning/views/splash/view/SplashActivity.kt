package com.example.thoughts_cleaning.views.splash.view

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.thoughts_cleaning.R
import com.example.thoughts_cleaning.base.MasilLeadActivity
import com.example.thoughts_cleaning.base.MoveEvent
import com.example.thoughts_cleaning.base.SplashEvent
import com.example.thoughts_cleaning.common.Constants
import com.example.thoughts_cleaning.common.vm.viewModelFactory
import com.example.thoughts_cleaning.databinding.ActivitySplashBinding
import com.example.thoughts_cleaning.views.main.view.activity.container.MainActivity
import com.example.thoughts_cleaning.views.splash.vm.SplashViewModel
import com.example.thoughts_cleaning.views.start.view.activity.container.StartActivity
import com.google.android.material.snackbar.Snackbar

class SplashActivity : MasilLeadActivity<ActivitySplashBinding, SplashViewModel>(R.layout.activity_splash) {

    override val viewModel by viewModelFactory { SplashViewModel() }

    private var isShowDialog = false

    private fun setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            //displayCutout
            //systemGestures
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()

        enableEdgeToEdge()
        setupWindowInsets()
        super.onCreate(savedInstanceState)

        //스플레쉬 화면 보여주기 여부
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            //android splash api 사용
            splashScreen.setKeepOnScreenCondition{!isShowDialog}
        }else{
            //splashActivity xml 사용
            splashScreen.setKeepOnScreenCondition{isShowDialog}
        }

        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        viewModel.apply { this@SplashActivity.setObserveLiveData() }
    }

    override fun setObserveLiveData() {
        super.setObserveLiveData()

        viewModel.moveEvent.observe(this@SplashActivity) {
            checkMove(if (it is MoveEvent.Splash) { it.moveType } else { SplashEvent.FINISH })
        }

        //설치 경로 수집
        //최초 한번만 진행
//        Log.d("getInstallReferrerData", (Prefs.getInstallReferrerDataCheck()))
//        if(Prefs.getInstallReferrerDataCheck() == ""){
//            viewModel.checkInstallReferrerData(this)
//        }

        //파라미터 확인하여 로그인 여부
        viewModel.checkParam()
    }

    private fun checkIntentExtras(sendIntent: Intent): Intent{
        //notification 알림을 통해서 들어왔을때 실행, 아니면 null
        intent.extras?.let { bundle ->
            val push = bundle.getSerializable(Constants.KEY_EVENT_PUSH)
            val newTag = bundle.getSerializable(Constants.KEY_EVENT_NEW_TAG)
            sendIntent.putExtra(Constants.KEY_EVENT_PUSH, push)
            sendIntent.putExtra(Constants.KEY_EVENT_NEW_TAG, newTag)
        }
        sendIntent.putExtra("comeState", "splash")

        return sendIntent
    }

    private fun checkMove(moveType: SplashEvent) {
        val intent = when (moveType) {
            SplashEvent.MAIN -> Intent(this, MainActivity::class.java)
            SplashEvent.LOGIN -> Intent(this, StartActivity::class.java)
//            SplashEvent.MAIN_WEARER -> checkIntentExtras(Intent(this, WearerActivity::class.java))
//            SplashEvent.MAIN_GUARDIAN -> {
//                checkIntentExtras(Intent(this, GuardianActivity::class.java))
//            }
            SplashEvent.FINISH -> null
//            SplashEvent.PERMISSION -> Intent(this, PermissionActivity::class.java)
        }

        intent?.let { goView(intent, true) } ?:
        run { showNetworkDialog { this.finish() } }
    }
}