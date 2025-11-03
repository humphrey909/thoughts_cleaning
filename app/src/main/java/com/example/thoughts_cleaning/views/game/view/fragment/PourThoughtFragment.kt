package com.example.thoughts_cleaning.views.game.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.thoughts_cleaning.R
import com.example.thoughts_cleaning.databinding.FragmentGameBinding
import com.example.thoughts_cleaning.databinding.FragmentPourThoughtBinding
import com.example.thoughts_cleaning.views.game.vm.fragment.GameFragmentViewModel
import com.example.thoughts_cleaning.views.game.vm.fragment.PourThoughtViewModel
import kotlin.getValue


class PourThoughtFragment : Fragment() {
    private val viewModel: PourThoughtViewModel by viewModels()

    private var _binding: FragmentPourThoughtBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_pour_thought, container, false)

        _binding = FragmentPourThoughtBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val context = requireContext()




    }
    override fun onResume() {
        super.onResume()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // 메모리 누수 방지
        _binding = null
    }
}


