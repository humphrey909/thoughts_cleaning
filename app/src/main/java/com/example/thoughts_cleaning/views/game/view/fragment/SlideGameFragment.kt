package com.example.thoughts_cleaning.views.game.view.fragment

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.thoughts_cleaning.R
import com.example.thoughts_cleaning.api.response.ResKindThoughtListDto
import com.example.thoughts_cleaning.base.MasilFragment
import com.example.thoughts_cleaning.common.vm.viewModelFactory
import com.example.thoughts_cleaning.databinding.FragmentGameBinding
import com.example.thoughts_cleaning.databinding.FragmentSlideGameBinding
import com.example.thoughts_cleaning.util.GameView
import com.example.thoughts_cleaning.views.game.vm.fragment.GameFragmentViewModel
import com.example.thoughts_cleaning.views.game.vm.fragment.SlideGameViewModel
import com.example.thoughts_cleaning.views.main.vm.fragment.MainFragmentViewModel.MainFlow


class SlideGameFragment : MasilFragment<FragmentSlideGameBinding, SlideGameViewModel>(R.layout.fragment_slide_game) {

    override val viewModel by viewModelFactory { SlideGameViewModel() }
    private var thoughtList: ResKindThoughtListDto? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSlideGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        thoughtList = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//            requireActivity().intent.getParcelableExtra("THOUGHT_LIST", ResKindThoughtListDto::class.java)
//        } else {
//            @Suppress("DEPRECATION")
//            requireActivity().intent.getParcelableExtra("THOUGHT_LIST")
//        }
//
//        // 데이터 확인
//        if (thoughtList != null) {
//            Log.d("GameActivity", "데이터 받기 성공: ${thoughtList}")
//        }

//        val idx = requireActivity().intent.getIntExtra("THOUGHT_IDX", -1)
//        val content = requireActivity().intent.getStringExtra("THOUGHT_CONTENT") ?: ""
//        viewModel.thoughtIdx = idx


//        val idxDetail = requireActivity().intent.getStringExtra("THOUGHT_DETAIL") ?: ""
//        viewModel.thoughtDetail = idxDetail


//        repeat(5) {
//            viewModel.ballLabels.add(idxDetail)
//        }
        handleNavigationEvent()

    }

    private fun handleNavigationEvent() {
        viewModel.getListThought()

        viewModel.thoughtListResponseData.observe(viewLifecycleOwner) { it ->
            if(it != null){
                Log.d("getListThought", "getListThought: ${it}")
//                binding.gameView.setBallData("불안해요")
                binding.gameView.setBallList(it)
            }
        }
    }

}