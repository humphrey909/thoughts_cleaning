package com.example.thoughts_cleaning.views.record_problem.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.thoughts_cleaning.R
import com.example.thoughts_cleaning.base.MasilFragment
import com.example.thoughts_cleaning.common.vm.viewModelFactory
import com.example.thoughts_cleaning.databinding.FragmentSelectKindDustBinding
import com.example.thoughts_cleaning.util.dialog.recent.CommonDialogBuilder
import com.example.thoughts_cleaning.util.dialog.recent.CommonDialogType
import com.example.thoughts_cleaning.views.record_problem.adapter.DustKindItemClickListener
import com.example.thoughts_cleaning.views.record_problem.adapter.DustKindListViewPagerAdapter
import com.example.thoughts_cleaning.views.record_problem.vm.fragment.SelectKindDustViewModel
import com.example.thoughts_cleaning.views.record_problem.vm.fragment.SelectKindDustViewModel.TypeFlow

class SelectKindDustFragment : MasilFragment<FragmentSelectKindDustBinding, SelectKindDustViewModel>(R.layout.fragment_select_kind_dust) {

    override val viewModel by viewModelFactory { SelectKindDustViewModel(mContext) }


    private var dustKindListadapter: DustKindListViewPagerAdapter? = null


//    private var _binding: FragmentSelectKindDustBinding? = null
//    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSelectKindDustBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        viewModelFactory = TrulyGenericViewModelFactory(mContext = mContext)
//        viewModel =
//            ViewModelProvider(this, viewModelFactory).get(SelectKindDustViewModel::class.java)


        dustKindListadapter = DustKindListViewPagerAdapter(viewModel.dustKindList, dustKindItemClickListener)
        binding.dustKindRecycler.adapter = dustKindListadapter

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        handleNavigationEvent()
    }


    private fun handleNavigationEvent() {
        viewModel.currentFlow.observe(viewLifecycleOwner) { flow ->
            when (flow) {
                TypeFlow.COMMON -> {

                }
                TypeFlow.NEXT_PAGE -> {

                    //예외처리 진행
                    if(viewModel.fixDustKind.value != null){
//                        findNavController().navigate(R.id.action_write_thought)

                        val action = SelectKindDustFragmentDirections.actionWriteThought(
                            kindThoughtIdx = viewModel.fixDustKind.value!!.index.toInt()
                        )
                        findNavController().navigate(action)

                        viewModel._currentFlow.postValue(TypeFlow.COMMON)
                    }else{
                        showDialog()
                    }
                }
                TypeFlow.BACK -> {
                    requireActivity().onBackPressedDispatcher.onBackPressed()
                }
            }
        }

        viewModel.dustKindListTotal.observe(viewLifecycleOwner) { flow ->
            if(flow != null){
                dustKindListadapter!!.updateData(viewModel.dustKindList)
            }
        }

        viewModel._dustFairyMessageText.postValue("오늘은 어떤 쓰레기를 버리고 싶으세요?")

        viewModel._currentFlow.postValue(TypeFlow.COMMON)

        viewModel.thoughtsKindList()
    }

    private fun showDialog(){
        val dialog = context?.let {
            CommonDialogBuilder(it, CommonDialogType.ONE_BUTTON)
                .title(getString(R.string.dialog_main_title))
                .main(getString(R.string.dialog_kind_dust_document))
                .onConfirmListener {

//                    //배터리 최적화 권한 허용 요청 알림 띄움
//                    val intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS)
//                    intent.setData(Uri.parse("package:${context?.packageName}"))
//                    startActivityForResult(intent, REQUEST_BATTERY_OPTIMIZATION_PERMISSION);
                }
                .build()
        }
        if (dialog != null) {
            showDialog(dialog)
        }
    }


    private val dustKindItemClickListener = DustKindItemClickListener { state, position ->
//        Log.i("dustKindItemClickListener", ": view tag = ${view.tag}")
//        Log.i("dustKindItemClickListener", ": item clicked $state")
//        Log.i("dustKindItemClickListener", ": item clicked $position")


        viewModel._fixDustKind.postValue(state)
//        viewModel.fixDustKind = state
    }
}