package com.example.thoughts_cleaning.views.game.view.activity.container

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.thoughts_cleaning.databinding.ActivityGameBinding
import com.example.thoughts_cleaning.views.game.vm.activity.container.GameActivityViewModel
import com.example.thoughts_cleaning.views.game.vm.fragment.GameFragmentViewModel
import kotlin.getValue

class GameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGameBinding
    private val viewModel: GameActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityGameBinding.inflate(layoutInflater)

        // 3. 바인딩 객체의 root 뷰를 Activity의 컨텐츠 뷰로 설정
        setContentView(binding.root)


    }
}