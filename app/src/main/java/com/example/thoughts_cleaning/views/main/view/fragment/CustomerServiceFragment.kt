package com.example.thoughts_cleaning.views.main.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.thoughts_cleaning.R
import com.example.thoughts_cleaning.base.MasilFragment
import com.example.thoughts_cleaning.common.vm.viewModelFactory
import com.example.thoughts_cleaning.databinding.FragmentCustomerServiceBinding
import com.example.thoughts_cleaning.databinding.FragmentTermsAndPoliciesBinding
import com.example.thoughts_cleaning.views.main.vm.fragment.CustomerServiceViewModel
import com.example.thoughts_cleaning.views.main.vm.fragment.TermsAndPoliciesViewModel


class CustomerServiceFragment :  MasilFragment<FragmentCustomerServiceBinding, CustomerServiceViewModel>(R.layout.fragment_customer_service) {

    override val viewModel by viewModelFactory { CustomerServiceViewModel() }

//    // 1. View Binding 객체 선언 (null 허용)
//    private var _binding: FragmentMySettingBinding? = null
//
//    // 2. 뷰가 살아있는 동안에만 접근할 수 있는 Non-null Binding 객체
//    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }



}