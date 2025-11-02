package com.example.thoughts_cleaning.views.start.view.activity.container

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.thoughts_cleaning.R
import com.example.thoughts_cleaning.databinding.ActivityMainBinding
import com.example.thoughts_cleaning.databinding.ActivityStartBinding
import com.example.thoughts_cleaning.views.main.vm.activity.container.MainActivityViewModel
import com.example.thoughts_cleaning.views.start.vm.activity.container.StartViewModel
import kotlin.getValue

class StartActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStartBinding
    private val viewModel: StartViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityStartBinding.inflate(layoutInflater)

        // 3. 바인딩 객체의 root 뷰를 Activity의 컨텐츠 뷰로 설정
        setContentView(binding.root)


    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
//        if (::gameView.isInitialized) gameView.pause()
        super.onPause()
    }
}