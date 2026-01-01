package com.example.thoughts_cleaning.views.record_problem.view.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.thoughts_cleaning.R
import com.example.thoughts_cleaning.base.MasilFragment
import com.example.thoughts_cleaning.common.TrulyGenericViewModelFactory
import com.example.thoughts_cleaning.common.vm.viewModelFactory
import com.example.thoughts_cleaning.databinding.FragmentRecordStateBinding
import com.example.thoughts_cleaning.databinding.FragmentSelectKindDustBinding
import com.example.thoughts_cleaning.util.dialog.recent.CommonDialogBuilder
import com.example.thoughts_cleaning.util.dialog.recent.CommonDialogType
import com.example.thoughts_cleaning.views.game.view.activity.container.GameActivity
import com.example.thoughts_cleaning.views.main.vm.fragment.MainFragmentViewModel.MainFlow
import com.example.thoughts_cleaning.views.record_problem.adapter.DustFeelingItemClickListener
import com.example.thoughts_cleaning.views.record_problem.adapter.DustFeelingListViewPagerAdapter
import com.example.thoughts_cleaning.views.record_problem.adapter.DustKindItemClickListener
import com.example.thoughts_cleaning.views.record_problem.adapter.DustKindListViewPagerAdapter
import com.example.thoughts_cleaning.views.record_problem.adapter.PeopleExItemClickListener
import com.example.thoughts_cleaning.views.record_problem.adapter.PeopleExViewPagerAdapter
import com.example.thoughts_cleaning.views.record_problem.vm.fragment.RecordStageFragmentViewModel
import com.example.thoughts_cleaning.views.record_problem.vm.fragment.RecordStageFragmentViewModel.RecordStageFlow
import com.example.thoughts_cleaning.views.record_problem.vm.fragment.SelectKindDustViewModel
import com.example.thoughts_cleaning.views.record_problem.vm.fragment.SelectKindDustViewModel.TypeFlow


class RecordStageFragment : MasilFragment<FragmentRecordStateBinding, RecordStageFragmentViewModel>(R.layout.fragment_record_state) {
//    lateinit var mContext: Context
//    private lateinit var viewModel: RecordStageFragmentViewModel

    override val viewModel by viewModelFactory { RecordStageFragmentViewModel(mContext) }

    private val args: RecordStageFragmentArgs by navArgs()

//    private var peopleExViewadapter: PeopleExViewPagerAdapter? = null
//    private var dustKindListadapter: DustKindListViewPagerAdapter? = null
//    private var dustFeelingListadapter: DustFeelingListViewPagerAdapter? = null

//    private lateinit var viewModelFactory: TrulyGenericViewModelFactory

    // 1. View Binding 객체 선언 (null 허용)
//    private var _binding: FragmentRecordStateBinding? = null
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
        _binding = FragmentRecordStateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //이부분 없으면 onclick이 동작하지 않음
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

//        viewModel._currentFlow.postValue(RecordStageFragmentViewModel.RecordStageFlow.COMMON)

        //선택한 생각 종류 데이터
        viewModel.kindThoughtIdx = args.kindThoughtIdx

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
        viewModel.currentFlow.observe(viewLifecycleOwner) { flow ->

            when (flow) {
                RecordStageFlow.COMMON -> {
                    viewModel._dustFairyMessageText.postValue("오늘은 어떤 쓰레기를 버리고 싶으세요?")
                }

                RecordStageFlow.NEXT_PAGE -> {
                    //예외처리 진행
//                    viewModel.fixDustDetail.value
//                    Log.d("currentMainFlow", "NEXT_PAGE: NEXT_PAGE"+ viewModel.fixDustDetail.value)
//                    Log.d("currentMainFlow", "NEXT_PAGE: NEXT_PAGE"+ viewModel.fixDustDetail.value?.length)

                    if(viewModel.fixDustDetail.value?.length == 0){
                        showCheckDataDialog()
                    }else{
                        nextPageBtn()
                    }
                }
                RecordStageFlow.BACK -> {
                    requireActivity().onBackPressedDispatcher.onBackPressed()
                }
            }

        }

        viewModel.fixDustDetail.observe(viewLifecycleOwner) { flow ->
            if(flow.length > 10){
                viewModel._dustFairyMessageText.postValue("잘하고 있어요. 더 털어 놓고 싶은 게 있나요?")
            }
        }

        viewModel.thoughtSaveIdx.observe(viewLifecycleOwner) { idx ->
            if(idx != 0){
                enterGame()
                viewModel._currentFlow.postValue(RecordStageFlow.COMMON)
            }
        }

        viewModel._currentFlow.postValue(RecordStageFragmentViewModel.RecordStageFlow.COMMON)
    }

    fun nextPageBtn(){
        showStartGameDialog()
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
        val dialog = context?.let {
            CommonDialogBuilder(it, CommonDialogType.TWO_BUTTON)
                .title(getString(R.string.dialog_main_title))
                .main(getString(R.string.dialog_game_start_document))
                .onConfirmListener {
                    viewModel.saveThought()
                }
                .build()
        }
        if (dialog != null) {
            showDialog(dialog)
        }
    }

    private fun enterGame(){
//        val intent = Intent(requireActivity(), GameActivity::class.java).apply {
//            putExtra("THOUGHT_IDX", viewModel.thoughtSaveIdx.value)
//        }

        val intent = Intent(requireActivity(), GameActivity::class.java).apply {
            // 1. IDX (Int)
            putExtra("THOUGHT_IDX", viewModel.thoughtSaveResponseData!!.idx)

            // 2. Content Thought (String)
            // viewModel에 해당 변수가 있다고 가정한 코드입니다. (실제 변수명으로 교체하세요)
            putExtra("THOUGHT_CONTENT", viewModel.thoughtSaveResponseData!!.contentThought)

            // 3. Name (String)
//            putExtra("KIND_NAME", viewModel.thoughtSaveResponseData!!.name)
//
//            // 4. Detail Text (String)
//            putExtra("KIND_DETAIL", viewModel.thoughtSaveResponseData!!.detailText)
        }
        startActivity(intent)
        requireActivity().finish()
    }

//    private fun enterGameExam(){
//        val intent = Intent(requireActivity(), GameActivity::class.java).apply {
//            putExtra("THOUGHT_DETAIL", viewModel.fixDustDetail.value)
//        }
//
//        startActivity(intent)
//        requireActivity().finish()
//    }

}