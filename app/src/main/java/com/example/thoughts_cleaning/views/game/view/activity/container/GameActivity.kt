package com.example.thoughts_cleaning.views.game.view.activity.container

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.example.thoughts_cleaning.R
import com.example.thoughts_cleaning.base.BaseNavigationActivity
import com.example.thoughts_cleaning.databinding.ActivityGameBinding
import com.example.thoughts_cleaning.util.base.BaseContract
import com.example.thoughts_cleaning.views.game.view.fragment.GameFragment
import com.example.thoughts_cleaning.views.game.vm.activity.container.GameActivityViewModel
import com.example.thoughts_cleaning.views.game.vm.fragment.GameFragmentViewModel
import kotlin.getValue

class GameActivity : BaseNavigationActivity(), BaseContract.NavMethod{
    override val appNameId: Int = R.string.app_name
    override val appLang: String = ""

    private lateinit var binding: ActivityGameBinding
    private val viewModel: GameActivityViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}