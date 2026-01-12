package com.example.thoughts_cleaning.views.main.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.example.thoughts_cleaning.R
import com.example.thoughts_cleaning.base.MasilFragment
import com.example.thoughts_cleaning.common.vm.viewModelFactory
import com.example.thoughts_cleaning.databinding.FragmentEmailQnaBinding
import com.example.thoughts_cleaning.databinding.FragmentMySettingBinding
import com.example.thoughts_cleaning.views.main.vm.fragment.EmailQnaViewModel
import com.example.thoughts_cleaning.views.main.vm.fragment.MySettingViewModel


class EmailQnaFragment :  MasilFragment<FragmentEmailQnaBinding, EmailQnaViewModel>(R.layout.fragment_email_qna) {
    override val viewModel by viewModelFactory { EmailQnaViewModel() }

    private lateinit var mWebView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEmailQnaBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        handleNavigationEvent()
    }

    private fun handleNavigationEvent() {


// Activity와 달리 view.findViewById를 사용해야 합니다.
        mWebView = binding.emailWebView

        // 웹뷰 설정
        val webSettings: WebSettings = mWebView.settings
        webSettings.javaScriptEnabled = true // 필수
        webSettings.setSupportZoom(true)
        webSettings.builtInZoomControls = true
        webSettings.displayZoomControls = false

        // WebViewClient 설정 (앱 내부에서 열리도록)
        mWebView.webViewClient = WebViewClient()

        // URL 로드
        val targetUrl = "https://mail.google.com/"
        mWebView.loadUrl(targetUrl)

        // 3. Fragment에서 뒤로가기 버튼 처리 (가장 중요!)
        // Activity의 onBackPressed()를 오버라이드할 수 없으므로 콜백을 등록해야 합니다.
//        requireActivity().onBackPressedDispatcher.addCallback(
//            viewLifecycleOwner, // 이 Fragment의 생명주기에 맞춥니다.
//            object : OnBackPressedCallback(true) { // true는 이 콜백이 활성화됨을 의미
//                override fun handleOnBackPressed() {
//                    if (mWebView.canGoBack()) {
//                        // 웹뷰 내 히스토리가 있으면 뒤로 가기
//                        mWebView.goBack()
//                    } else {
//                        // 웹뷰 히스토리가 없으면 이 콜백을 비활성화하고
//                        // 기본 시스템 뒤로가기 동작을 수행 (앱 종료 또는 이전 프래그먼트로 이동)
//                        isEnabled = false
//                        requireActivity().onBackPressedDispatcher.onBackPressed()
//                    }
//                }
//            }
//        )


    }
}