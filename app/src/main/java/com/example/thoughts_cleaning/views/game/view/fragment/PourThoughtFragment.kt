package com.example.thoughts_cleaning.views.game.view.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.thoughts_cleaning.R
import com.example.thoughts_cleaning.api.Prefs
import com.example.thoughts_cleaning.databinding.FragmentPourThoughtBinding
import com.example.thoughts_cleaning.views.game.vm.fragment.PourThoughtViewModel
import com.example.thoughts_cleaning.views.game.vm.fragment.PourThoughtViewModel.PourThoughtViewFlow
import kotlin.getValue


class PourThoughtFragment : Fragment() {
    private val viewModel: PourThoughtViewModel by viewModels()

    private var _binding: FragmentPourThoughtBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_pour_thought, container, false)

        _binding = FragmentPourThoughtBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        val context = requireContext()

        //이부분 없으면 onclick이 동작하지 않음
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel._currentMainFlow.postValue(PourThoughtViewFlow.COMMON)

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

    private fun handleNavigationEvent() {
        viewModel.currentMainFlow.observe(viewLifecycleOwner) { flow ->

//            Log.d("currentMainFlow", "ENTER_GAME: ENTER_GAME")
//            Log.d("currentMainFlow", "ENTER_GAME: $flow")



            when (flow) {
                PourThoughtViewFlow.COMMON -> {  }
                PourThoughtViewFlow.NEXT_PAGE -> {

//                    binding.editTextBox.
                    if(binding.editTextBox.text.isNotEmpty()){
                        Log.d("currentMainFlow", "ENTER_GAME: ${binding.editTextBox.text}")
//                    Log.d("currentMainFlow", "ENTER_GAME1: ${Prefs.anxietyWriteList}")
//
//                    Prefs.anxietyWrite = binding.editTextBox.text.toString()
//
//                    Log.d("currentMainFlow", "ENTER_GAME2: ${Prefs.anxietyWrite}")


                        selectAnxietyWriteText()
                        addAnxietyWriteText(binding.editTextBox.text.toString())
                        val list = selectAnxietyWriteText()

                        viewModel._anxietyWriteListSize.postValue(list.size)

                        binding.editTextBox.text.clear()
                    }
                }
                PourThoughtViewFlow.QUIT_PAGE -> {
                    val bundle = bundleOf("waste_count" to viewModel.anxietyWriteListSize.value)


                    val navController = findNavController()
                    navController.navigate(R.id.action_quit_page, bundle)
                }
            }
        }
    }

    private fun addAnxietyWriteText(anxietyWrite: String) {
        val currentList: ArrayList<String> = Prefs.anxietyWriteList
        currentList.add(anxietyWrite)
        Prefs.anxietyWriteList = currentList
    }

    private fun selectAnxietyWriteText(): ArrayList<String> {
        val records: ArrayList<String> = Prefs.anxietyWriteList

        Log.d("PREF", "기록 목록: $records")

        return records
    }
}


