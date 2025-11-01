package com.example.thoughts_cleaning.views.main.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.thoughts_cleaning.R
import com.example.thoughts_cleaning.databinding.FragmentMainBinding
import com.example.thoughts_cleaning.databinding.FragmentMySettingBinding


class MySettingFragment : Fragment() {

    // 1. View Binding 객체 선언 (null 허용)
    private var _binding: FragmentMySettingBinding? = null

    // 2. 뷰가 살아있는 동안에만 접근할 수 있는 Non-null Binding 객체
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMySettingBinding.inflate(inflater, container, false)

        return binding.root
    }
}