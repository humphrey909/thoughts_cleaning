package com.example.thoughts_cleaning.views.game.view.fragment

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.thoughts_cleaning.R
import com.example.thoughts_cleaning.base.MasilFragment
import com.example.thoughts_cleaning.base.MoveEvent
import com.example.thoughts_cleaning.common.vm.viewModelFactory
import com.example.thoughts_cleaning.databinding.FragmentGameBinding
import com.example.thoughts_cleaning.databinding.FragmentPourThoughtBinding
import com.example.thoughts_cleaning.databinding.FragmentWindowBinding
import com.example.thoughts_cleaning.util.GameView
import com.example.thoughts_cleaning.views.game.GameEvent
import com.example.thoughts_cleaning.views.game.WindowCleanEvent
import com.example.thoughts_cleaning.views.game.vm.fragment.GameFragmentViewModel
import com.example.thoughts_cleaning.views.game.vm.fragment.PourThoughtViewModel
import com.example.thoughts_cleaning.views.game.vm.fragment.PourThoughtViewModel.PourThoughtViewFlow
import com.example.thoughts_cleaning.views.game.vm.fragment.WindowViewModel
import kotlin.getValue


class WindowFragment : MasilFragment<FragmentWindowBinding, WindowViewModel>(R.layout.fragment_window){
    override val viewModel by viewModelFactory { WindowViewModel() }

//    private val viewModel: WindowViewModel by viewModels()

//    private var _binding: FragmentWindowBinding? = null
//    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWindowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        val context = requireContext()

        //이부분 없으면 onclick이 동작하지 않음
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel._moveEvent.postValue(MoveEvent.WindowClean(WindowCleanEvent.COMMON))

        handleNavigationEvent()




    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // 메모리 누수 방지
        _binding = null
    }

    private fun handleNavigationEvent() {

        viewModel.moveEvent.observe(this.viewLifecycleOwner) {
            if (it is MoveEvent.WindowClean) {
                when(it.moveType) {
                    WindowCleanEvent.COMMON -> {

                    }

                    WindowCleanEvent.NEXT_PAGE -> TODO()
                    WindowCleanEvent.QUIT_PAGE -> TODO()
                    WindowCleanEvent.SOLUTION -> {
                        binding.drawingView.setTouchImage(it.moveType, R.drawable.clean_window_tool2)
                    }
                    WindowCleanEvent.WASHER -> {
                        Log.d("currentMainFlow", "ENTER_GAME2_2: ")
                        binding.drawingView.setTouchImage(it.moveType, R.drawable.clean_window_tool1)

                    }
                }
            }
        }



        val imageBitmap = BitmapFactory.decodeResource(resources, R.drawable.smudge_texture2)
        binding.drawingView.setImage(imageBitmap)

    }
}