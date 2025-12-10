package com.example.thoughts_cleaning.views.record_problem.view.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.thoughts_cleaning.R
import com.example.thoughts_cleaning.common.TrulyGenericViewModelFactory
import com.example.thoughts_cleaning.databinding.FragmentSelectKindDustBinding
import com.example.thoughts_cleaning.views.record_problem.adapter.DustKindItemClickListener
import com.example.thoughts_cleaning.views.record_problem.adapter.DustKindListViewPagerAdapter
import com.example.thoughts_cleaning.views.record_problem.vm.fragment.SelectKindDustViewModel
import com.example.thoughts_cleaning.views.record_problem.vm.fragment.SelectKindDustViewModel.TypeFlow


class SelectKindDustFragment : Fragment() {
    lateinit var mContext: Context
    private lateinit var viewModel: SelectKindDustViewModel
    private lateinit var viewModelFactory: TrulyGenericViewModelFactory

    private var dustKindListadapter: DustKindListViewPagerAdapter? = null


    private var _binding: FragmentSelectKindDustBinding? = null
    private val binding get() = _binding!!

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

        viewModelFactory = TrulyGenericViewModelFactory(mContext = mContext)
        viewModel =
            ViewModelProvider(this, viewModelFactory).get(SelectKindDustViewModel::class.java)


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
                    viewModel._dustFairyMessageText.postValue("오늘은 어떤 쓰레기를 버리고 싶으세요?")
                }
                TypeFlow.NEXT_PAGE -> {
                    findNavController().navigate(R.id.action_write_thought)
                    viewModel._currentFlow.postValue(TypeFlow.COMMON)
                }
                TypeFlow.BACK -> {
                    requireActivity().onBackPressedDispatcher.onBackPressed()
                }
            }
        }

        viewModel._dustFairyMessageText.postValue("오늘은 어떤 쓰레기를 버리고 싶으세요?")

        viewModel._currentFlow.postValue(TypeFlow.COMMON)
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)

        mContext = context
    }

    private val dustKindItemClickListener = DustKindItemClickListener { state, position ->
//        Log.i("dustKindItemClickListener", ": view tag = ${view.tag}")
//        Log.i("dustKindItemClickListener", ": item clicked $state")
//        Log.i("dustKindItemClickListener", ": item clicked $position")


        viewModel._fixDustKind.postValue(state)
//        viewModel.fixDustKind = state
    }
}