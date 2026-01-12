package com.example.thoughts_cleaning.views.main.view.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.thoughts_cleaning.R
import com.example.thoughts_cleaning.api.Prefs
import com.example.thoughts_cleaning.base.MasilFragment
import com.example.thoughts_cleaning.base.MoveEvent
import com.example.thoughts_cleaning.common.vm.viewModelFactory
import com.example.thoughts_cleaning.databinding.FragmentMySettingBinding
import com.example.thoughts_cleaning.databinding.FragmentUserInfoBinding
import com.example.thoughts_cleaning.util.dialog.recent.CommonDialogBuilder
import com.example.thoughts_cleaning.util.dialog.recent.CommonDialogType
import com.example.thoughts_cleaning.views.main.SettingEvent
import com.example.thoughts_cleaning.views.main.UserInfoEvent
import com.example.thoughts_cleaning.views.main.vm.fragment.MySettingViewModel
import com.example.thoughts_cleaning.views.main.vm.fragment.UserInfoViewModel
import com.example.thoughts_cleaning.views.start.view.activity.container.StartActivity

class UserInfoFragment :  MasilFragment<FragmentUserInfoBinding, UserInfoViewModel>(R.layout.fragment_user_info) {

    override val viewModel by viewModelFactory { UserInfoViewModel() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUserInfoBinding.inflate(inflater, container, false)

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
            if (it is MoveEvent.UserInfo) {
                when (it.moveType) {
                    UserInfoEvent.COMMON -> {
                    }
                    UserInfoEvent.USER_DELETE -> {
                        showDialogFinishTwoButton()
                    }
                    UserInfoEvent.BACK -> {
                        requireActivity().onBackPressedDispatcher.onBackPressed()
                    }
                    UserInfoEvent.GO_LOGIN -> {
                        enterLogin()
                    }
                }
            }
        }


        viewModel._name.postValue(Prefs.name)
        viewModel._email.postValue(Prefs.email)
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
                .main(getString(R.string.dialog_user_delete_document))
                .onConfirmListener {
                    viewModel.withdraw(UserInfoEvent.GO_LOGIN)
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