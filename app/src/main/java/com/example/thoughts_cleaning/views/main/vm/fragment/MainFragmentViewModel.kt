package com.example.thoughts_cleaning.views.main.vm.fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.thoughts_cleaning.R

class MainFragmentViewModel: ViewModel() {

    private val _characterImageResId = MutableLiveData<Int>(R.drawable.character_default)
    val characterImages: LiveData<Int> = _characterImageResId

    // 질문 리스트
    val allQuestions = mutableListOf(
        // ------------------------------------
        // 불안 (Anxiety) 테마
        // ------------------------------------
        "지금 당신을 가장 '멈칫'하게 만드는 걱정거리 하나를 꼽는다면 무엇인가요?",
        "불안함이 당신에게 속삭이는 말 중, 오늘은 특별히 '믿지 않겠다'고 결정할 수 있는 말은 무엇인가요?",
        "최근에 불안 때문에 미루었지만, 5분 안에 시작할 수 있는 작은 행동은 무엇인가요?",
        // ------------------------------------
        // 비교 (Comparison) 테마
        // ------------------------------------
        "타인과 나를 비교하는 순간, 내가 가진 장점 중 가장 쉽게 잊어버리게 되는 것은 무엇인가요?",
        "다른 사람의 성공을 봤을 때, 부러움 대신 '나도 잘하고 있다'고 스스로에게 말해줄 수 있는 나의 영역은 무엇인가요?",
        "나만의 속도와 방식으로 성취했던 최근의 경험 하나를 떠올려 보세요.",
        // ------------------------------------
        // 시선 (External Gaze/Judgment) 테마
        // ------------------------------------
        "다른 사람의 시선 때문에 시도하지 못했던 일 중, 사실은 당신이 가장 하고 싶었던 일은 무엇인가요?",
        "주변의 평가가 아닌, '나 자신'이 정의하는 '나의 성공'은 어떤 모습인가요?",
        "만약 당신이 아무도 모르는 곳에서 하루를 보낸다면, 오늘 가장 자유롭게 하고 싶은 행동 2가지는 무엇인가요?",
        // ------------------------------------
        // 통합/긍정 유도
        // ------------------------------------
        "이 모든 생각(불안, 비교, 시선)에도 불구하고, 지금 이 순간 감사할 수 있는 아주 작은 것 하나를 찾아보세요."
    )
    // 이미 사용된 질문을 저장하는 Set
    val askedQuestions = mutableSetOf<String>()




}