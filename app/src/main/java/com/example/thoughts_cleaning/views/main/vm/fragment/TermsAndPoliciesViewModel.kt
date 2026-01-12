package com.example.thoughts_cleaning.views.main.vm.fragment

import com.example.thoughts_cleaning.base.MoveEvent
import com.example.thoughts_cleaning.base.MoveEvent.Setting
import com.example.thoughts_cleaning.common.vm.MasilViewModel
import com.example.thoughts_cleaning.views.main.SettingEvent
import com.example.thoughts_cleaning.views.main.TermsAndPoliciesEvent

class TermsAndPoliciesViewModel: MasilViewModel() {



    fun onClick(event: TermsAndPoliciesEvent) {
        _moveEvent.postValue(MoveEvent.TermsAndPolicies(event))
//        when (event) {
//
//            TermsAndPoliciesEvent.COMMON -> TODO()
//            TermsAndPoliciesEvent.BACK -> {
//                _moveEvent.postValue(MoveEvent.TermsAndPolicies(event))
//            }
//            TermsAndPoliciesEvent.TERMS_OF_SERVICE -> TODO()
//            TermsAndPoliciesEvent.PRIVACY_POLICY -> TODO()
//            TermsAndPoliciesEvent.CONTACT_US -> TODO()
//        }
    }
}