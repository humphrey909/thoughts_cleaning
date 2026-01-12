package com.example.thoughts_cleaning.views.main.view.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.example.thoughts_cleaning.R
import com.example.thoughts_cleaning.api.request.SocialKakaoLoginRequestData
import com.example.thoughts_cleaning.base.MasilFragment
import com.example.thoughts_cleaning.base.MoveEvent
import com.example.thoughts_cleaning.base.MoveEvent.Setting
import com.example.thoughts_cleaning.common.extension.call
import com.example.thoughts_cleaning.common.vm.viewModelFactory
import com.example.thoughts_cleaning.databinding.FragmentMainBinding
import com.example.thoughts_cleaning.databinding.FragmentMySettingBinding
import com.example.thoughts_cleaning.util.dialog.recent.CommonDialogBuilder
import com.example.thoughts_cleaning.util.dialog.recent.CommonDialogType
import com.example.thoughts_cleaning.views.main.SettingEvent
import com.example.thoughts_cleaning.views.main.view.activity.container.MainActivity
import com.example.thoughts_cleaning.views.main.vm.fragment.MainFragmentViewModel
import com.example.thoughts_cleaning.views.main.vm.fragment.MainFragmentViewModel.MainFlow
import com.example.thoughts_cleaning.views.main.vm.fragment.MySettingViewModel
import com.example.thoughts_cleaning.views.start.LoginEvent
import com.example.thoughts_cleaning.views.start.view.activity.container.StartActivity
import kotlinx.coroutines.launch


class MySettingFragment :  MasilFragment<FragmentMySettingBinding, MySettingViewModel>(R.layout.fragment_my_setting) {

    override val viewModel by viewModelFactory { MySettingViewModel() }

//    // 1. View Binding 객체 선언 (null 허용)
//    private var _binding: FragmentMySettingBinding? = null
//
//    // 2. 뷰가 살아있는 동안에만 접근할 수 있는 Non-null Binding 객체
//    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMySettingBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        handleNavigationEvent()
    }

    private fun handleNavigationEvent() {

        viewModel.moveEvent.observe(this.viewLifecycleOwner) {
            if (it is MoveEvent.Setting) {
                when (it.moveType) {
                    SettingEvent.COMMON -> {
                    }
                    SettingEvent.USER_INFO -> {
                    }
                    SettingEvent.LOGOUT -> {
                        showDialogFinishTwoButton()
                    }
                    SettingEvent.GO_LOGIN -> {
                        enterLogin()
                    }
                    SettingEvent.BACK -> {
                        requireActivity().onBackPressedDispatcher.onBackPressed()
                    }

                    SettingEvent.USERINFO -> {
                        findNavController().navigate(R.id.user_info_fragment)
                        viewModel._moveEvent.postValue(Setting(SettingEvent.COMMON))
                    }

                    SettingEvent.TERMS_AND_POLICIES -> {
                        findNavController().navigate(R.id.terms_and_policies_fragment)
                        viewModel._moveEvent.postValue(Setting(SettingEvent.COMMON))
                    }

                    SettingEvent.EMAIL_QNA -> {
                        // 버튼 클릭 리스너 등에서 사용
                        val intent = Intent(Intent.ACTION_SENDTO).apply {
                            data = Uri.parse("mailto:humphrey1858@gmail.com") // 받는 사람
                            putExtra(Intent.EXTRA_SUBJECT, "생각청소 문의사항") // 제목
                            putExtra(Intent.EXTRA_TEXT, "내용을 입력해주세요.") // 본문
                        }
                        startActivity(intent)

//                        findNavController().navigate(R.id.email_qna_fragment)
//                        viewModel._moveEvent.postValue(Setting(SettingEvent.COMMON))
                    }
                }
            }
        }
    }

    //login 페이지로 이동
    fun enterLogin(){
        val intent = Intent(requireActivity(), StartActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

    private fun showDialogFinishTwoButton(){
        val dialog = context?.let {
            CommonDialogBuilder(it, CommonDialogType.TWO_BUTTON_GAME)
                .title(getString(R.string.dialog_main_title))
                .main(getString(R.string.dialog_logout_document))
                .onConfirmListener {
                    viewModel.logout(SettingEvent.GO_LOGIN)
                }
                .onCancelListener{

                }
                .build()
        }
        if (dialog != null) {
            showDialog(dialog)
        }
    }
}
