package com.example.thoughts_cleaning.views.game.view.activity.container

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.example.thoughts_cleaning.R
import com.example.thoughts_cleaning.databinding.ActivityGameBinding
import com.example.thoughts_cleaning.views.game.view.fragment.GameFragment
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


        //GameFragment 데이터 전송 로직
//        val wasteCount = intent.getIntExtra("waste_count", 0)
//        Log.d("waste_count", "waste_count: ${wasteCount}")
//
//
//        val bundle = Bundle().apply {
//            putString("waste_count", wasteCount.toString())
//        }


//        if (savedInstanceState == null) {
//            val bFragment = GameFragment.newInstance(wasteCount.toString())
//        Log.d("waste_count", "bFragment: ${bFragment}")

//            supportFragmentManager.beginTransaction()
//                .replace(R.id.GameFragment, bFragment)
//                .commit()
//        }
//        if (savedInstanceState == null) {
//            val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment
//            navHostFragment.navController.setGraph(R.navigation.nav_game, bundle)
//
//        }

    }
}