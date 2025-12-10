package com.example.thoughts_cleaning.base

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.os.bundleOf
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import com.example.thoughts_cleaning.R
import com.example.thoughts_cleaning.common.ErrorCodeApp
import com.example.thoughts_cleaning.common.util.showToast
import com.example.thoughts_cleaning.common.vm.BaseViewModel
import com.example.thoughts_cleaning.common.vm.SuniException
import com.example.thoughts_cleaning.views.start.view.activity.container.StartActivity
import com.navercorp.nid.NaverIdLoginSDK
import kotlin.jvm.java
import kotlin.let
import kotlin.to

/**
 *
 */
abstract class MasilFragment<T: ViewDataBinding, S: BaseViewModel>(@LayoutRes layoutId: Int) : BaseFragment<T, S>(layoutId) {
    open val isLightStatusBar = true
    open val statusBarColor = R.color.white
//    private lateinit var firebaseAnalytics: FirebaseAnalytics

    lateinit var mContext: Context

    override val customAppEvent: Observer<ErrorCodeApp?>? = null
    override val customServerEvent: Observer<Exception>? = Observer<Exception> {
        when (it) {
            is SuniException.InvalidLanguageException,
            is SuniException.ExpiredSessionException,
            is SuniException.InvalidSessionException -> {
//                Prefs.initUserInfo()

                showErrorToast(ErrorCodeApp.SESSION_INVALID)
                val intent = Intent(activity, StartActivity::class.java)
                goView(intent, true)
            }
            else -> processGeneralException(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        //여기서 에러남 체크 바람 20240522
//        /** Naver Login Module Initialize */
//        val naverClientId = getString(R.string.naver_login_client_id)
//        val naverClientSecret = getString(R.string.naver_login_client_secret)
//        val naverClientName = getString(R.string.naver_login_client_name)
//        NaverIdLoginSDK.initialize(mContext, naverClientId, naverClientSecret , naverClientName)

//        this.firebaseAnalytics = Firebase.analytics
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        mContext = context
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.setStatusBarColor(isLightStatusBar, statusBarColor)
    }

    override fun onResume() {
        super.onResume()

        setAnalyticsScreen()
    }

    private fun setAnalyticsScreen() {
        val className = this::class.java.simpleName
//        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_ITEM) {
//            param(FirebaseAnalytics.Param.SCREEN_NAME, className)
//            param(FirebaseAnalytics.Param.SCREEN_CLASS, className)
//        }
    }


    private fun showMessage(exception: Exception) {
        exception.message?.let { msg -> context?.showToast(msg) }
    }

    open fun processGeneralException(exception: Exception) {
        showMessage(exception)
    }

    //상점 이용약관 동의 페이지 이동
//    fun onMoveTermsOfUse(){
//        navigate(R.id.action_wearer_app_info_to_privacy, bundleOf( "term" to Term(TermType.SERVICE, "https://dnx-a.s3.ap-northeast-2.amazonaws.com/touchcare/prod/term/shopping_ko.html")))
//    }
}