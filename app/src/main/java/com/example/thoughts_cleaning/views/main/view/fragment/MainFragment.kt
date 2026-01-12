package com.example.thoughts_cleaning.views.main.view.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.thoughts_cleaning.R
import com.example.thoughts_cleaning.base.MasilFragment
import com.example.thoughts_cleaning.common.vm.viewModelFactory
import com.example.thoughts_cleaning.views.main.vm.fragment.MainFragmentViewModel
import com.example.thoughts_cleaning.databinding.FragmentMainBinding
import com.example.thoughts_cleaning.util.dialog.recent.CommonDialogBuilder
import com.example.thoughts_cleaning.util.dialog.recent.CommonDialogType
import com.example.thoughts_cleaning.views.game.view.activity.container.GameActivity
import com.example.thoughts_cleaning.views.main.vm.fragment.MainFragmentViewModel.MainFlow

//import com.example.thoughts_cleaning.views.game.vm.fragment.GameViewModel.MainFlow

class MainFragment : MasilFragment<FragmentMainBinding, MainFragmentViewModel>(R.layout.fragment_main) {

    override val viewModel by viewModelFactory { MainFragmentViewModel() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //이부분 없으면 onclick이 동작하지 않음
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel._currentMainFlow.postValue(MainFlow.COMMON)
        viewModel._dustFairyMessageText.postValue(getString(R.string.ex_main_title))

        handleNavigationEvent()
    }

    override fun onResume() {
        super.onResume()

        viewModel.fixDustDetail.value = ""
        viewModel.getListThought()

        viewModel._thoughtSaveIdx.postValue(0)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // 메모리 누수 방지
        _binding = null
    }

//    fun showCustomDialog() {
////        val dialog = dialogCustom()
////        // supportFragmentManager 또는 childFragmentManager를 사용합니다.
////        dialog.show(supportFragmentManager, "MyCustomDialogTag")
//
//        // 1. 아직 묻지 않은 질문만 필터링
//        val remainingQuestions = viewModel.allQuestions.filter { it !in viewModel.askedQuestions }
//
//        if (remainingQuestions.isNotEmpty()) {
//            // 2. 남은 질문 중 랜덤으로 하나 선택
//            val nextQuestion = remainingQuestions.random()
//
//            // 3. 질문 사용 처리
//            viewModel.askedQuestions.add(nextQuestion)
//
//            // 4. DialogFragment 생성 및 표시
//            val dialog = QuestionInputDialog.Companion.newInstance(nextQuestion)
//            dialog.show(requireActivity().supportFragmentManager, "QuestionDialog")
//
//        } else {
//            // 모든 질문을 다 소진했을 때의 처리
//            Toast.makeText(requireContext(), "모든 질문을 완료했습니다!", Toast.LENGTH_LONG).show()
//        }
//    }

//
//    private fun initNavigation() {
//
//        binding.bottomNavInFragment.itemIconTintList = null
//        binding.bottomNavInFragment.run {
//            setOnItemSelectedListener { item ->
////                if(tabMenuSelectComplete == 0){
//                    when (item.itemId) {
////                        R.id.home_button -> {
////                            //결제 전, 후 페이지
//////                            viewModel._bottomSheetMenuValue.postValue(BottomSheetState.HOME)
////                        }
////                        R.id.set_button -> {
////                        }
//                }
//                true
//            }
//        }
//
//    }


    // ViewModel의 이벤트에 따라 실제 화면 전환(Intent)을 처리하는 함수
    private fun handleNavigationEvent() {
        viewModel.currentMainFlow.observe(viewLifecycleOwner) { flow ->
            when (flow) {
                MainFlow.COMMON -> { }
                MainFlow.ENTER_GAME -> {
                    if(viewModel.fixDustDetail.value?.length == 0){
                        showCheckDataDialog()
                    }else{
                        showStartGameDialog()
//                        nextPageBtn()
                    }
                }
//                MainFlow.RECORD_PROBLEM -> {
//                    enter_game()
//                }
                MainFlow.SETTING -> {
                    findNavController().navigate(R.id.setting_fragment)
                    viewModel._currentMainFlow.postValue(MainFlow.COMMON)
                }
            }
        }

        viewModel.thoughtSaveIdx.observe(viewLifecycleOwner) { idx ->
            if(idx != 0){
                enterGame()
                viewModel._currentMainFlow.postValue(MainFlow.COMMON)
            }
        }

//        viewModel.getListThought()
    }

    private fun showCheckDataDialog(){
        val dialog = context?.let {
            CommonDialogBuilder(it, CommonDialogType.ONE_BUTTON)
                .title(getString(R.string.dialog_main_title))
                .main(getString(R.string.dialog_record_stage_document))
                .onConfirmListener {

                }
                .build()
        }
        if (dialog != null) {
            showDialog(dialog)
        }
    }


    //게임 시작 알림
    private fun showStartGameDialog(){
        viewModel.saveThought()

//        val dialog = context?.let {
//            CommonDialogBuilder(it, CommonDialogType.TWO_BUTTON)
//                .title(getString(R.string.dialog_main_title))
//                .main(getString(R.string.dialog_game_start_document))
//                .onConfirmListener {
//                    viewModel.saveThought()
//                }
//                .build()
//        }
//        if (dialog != null) {
//            showDialog(dialog)
//        }
    }

    fun enterGame(){
        Log.d("ScreenSize", "화면 높이: ENTER_GAME")
        //thoughtListResponseData
//        viewModel.thoughtListResponseData!!.kindThoughtList.size

        val intent = Intent(requireActivity(), GameActivity::class.java)
//        val intent = Intent(requireActivity(), GameActivity::class.java).apply {
////            putExtra("THOUGHT_CONTENT", viewModel.thoughtSaveResponseData!!.contentThought)
//            putExtra("THOUGHT_LIST", viewModel.thoughtListResponseData)
//        }
        startActivity(intent)

        viewModel._currentMainFlow.postValue(MainFlow.COMMON)
    }
}