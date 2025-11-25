package com.example.thoughts_cleaning.views.game.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.thoughts_cleaning.R
import com.example.thoughts_cleaning.databinding.FragmentPourThoughtBinding
import com.example.thoughts_cleaning.databinding.FragmentWindowBinding
import com.example.thoughts_cleaning.views.game.vm.fragment.PourThoughtViewModel
import com.example.thoughts_cleaning.views.game.vm.fragment.PourThoughtViewModel.PourThoughtViewFlow
import com.example.thoughts_cleaning.views.game.vm.fragment.WindowViewModel
import kotlin.getValue


class WindowFragment : Fragment() {
    private val viewModel: WindowViewModel by viewModels()

    private var _binding: FragmentWindowBinding? = null
    private val binding get() = _binding!!

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

        viewModel._currentMainFlow.postValue(WindowViewModel.WindowViewFlow.COMMON)

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

    }
}