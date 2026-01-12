package com.example.thoughts_cleaning.views.main.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.thoughts_cleaning.R
import com.example.thoughts_cleaning.base.MasilFragment
import com.example.thoughts_cleaning.base.MoveEvent
import com.example.thoughts_cleaning.base.MoveEvent.Setting
import com.example.thoughts_cleaning.common.vm.viewModelFactory
import com.example.thoughts_cleaning.databinding.FragmentMySettingBinding
import com.example.thoughts_cleaning.databinding.FragmentTermsAndPoliciesBinding
import com.example.thoughts_cleaning.views.main.SettingEvent
import com.example.thoughts_cleaning.views.main.TermsAndPoliciesEvent
import com.example.thoughts_cleaning.views.main.vm.fragment.MySettingViewModel
import com.example.thoughts_cleaning.views.main.vm.fragment.TermsAndPoliciesViewModel

class TermsAndPoliciesFragment :  MasilFragment<FragmentTermsAndPoliciesBinding, TermsAndPoliciesViewModel>(R.layout.fragment_terms_and_policies) {

    override val viewModel by viewModelFactory { TermsAndPoliciesViewModel() }

//    // 1. View Binding 객체 선언 (null 허용)
//    private var _binding: FragmentMySettingBinding? = null
//
//    // 2. 뷰가 살아있는 동안에만 접근할 수 있는 Non-null Binding 객체
//    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        handleNavigationEvent()
    }

    private fun handleNavigationEvent() {
        viewModel.moveEvent.observe(this.viewLifecycleOwner) {
            if (it is MoveEvent.TermsAndPolicies) {
                when (it.moveType) {
//                    TermsAndPoliciesEvent.COMMON -> {
//                    }
//                    TermsAndPoliciesEvent.USER_INFO -> {
//                    }
//                    TermsAndPoliciesEvent.LOGOUT -> {
//                        showDialogFinishTwoButton()
//                    }
//                    SettingEvent.GO_LOGIN -> {
//                        enterLogin()
//                    }
//                    SettingEvent.BACK -> {
//                        requireActivity().onBackPressedDispatcher.onBackPressed()
//                    }
//
//                    SettingEvent.USERINFO -> {
//                        findNavController().navigate(R.id.user_info_fragment)
//                        viewModel._moveEvent.postValue(Setting(SettingEvent.COMMON))
//                    }
//
//                    SettingEvent.TERMS_AND_POLICIES -> {
//                        findNavController().navigate(R.id.terms_and_policies_fragment)
//                        viewModel._moveEvent.postValue(Setting(SettingEvent.COMMON))
//                    }
                    TermsAndPoliciesEvent.COMMON -> TODO()
                    TermsAndPoliciesEvent.BACK -> {
                        requireActivity().onBackPressedDispatcher.onBackPressed()
                    }
                    TermsAndPoliciesEvent.TERMS_OF_SERVICE -> TODO()
                    TermsAndPoliciesEvent.PRIVACY_POLICY -> TODO()
                    TermsAndPoliciesEvent.CONTACT_US -> TODO()
                }
            }
        }
    }
}