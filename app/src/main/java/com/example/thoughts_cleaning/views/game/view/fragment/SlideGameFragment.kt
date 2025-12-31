package com.example.thoughts_cleaning.views.game.view.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.thoughts_cleaning.R
import com.example.thoughts_cleaning.base.MasilFragment
import com.example.thoughts_cleaning.common.vm.viewModelFactory
import com.example.thoughts_cleaning.databinding.FragmentGameBinding
import com.example.thoughts_cleaning.databinding.FragmentSlideGameBinding
import com.example.thoughts_cleaning.util.GameView
import com.example.thoughts_cleaning.views.game.vm.fragment.GameFragmentViewModel
import com.example.thoughts_cleaning.views.game.vm.fragment.SlideGameViewModel


class SlideGameFragment : MasilFragment<FragmentSlideGameBinding, SlideGameViewModel>(R.layout.fragment_slide_game) {

    override val viewModel by viewModelFactory { SlideGameViewModel() }


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

        val idx = requireActivity().intent.getIntExtra("THOUGHT_IDX", -1)
        val content = requireActivity().intent.getStringExtra("THOUGHT_CONTENT") ?: ""
        val kindName = requireActivity().intent.getStringExtra("KIND_NAME") ?: ""
        val kindDetail = requireActivity().intent.getStringExtra("KIND_DETAIL") ?: ""
        viewModel.thoughtIdx = idx


//        val idxDetail = requireActivity().intent.getStringExtra("THOUGHT_DETAIL") ?: ""
//        viewModel.thoughtDetail = idxDetail


//        repeat(5) {
//            viewModel.ballLabels.add(idxDetail)
//        }

        binding.gameView.setBallData(content, kindName, kindDetail)

    }

}