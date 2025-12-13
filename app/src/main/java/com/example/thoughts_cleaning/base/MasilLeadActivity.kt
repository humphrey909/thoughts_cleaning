package com.example.thoughts_cleaning.base

import android.content.Intent
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import com.example.thoughts_cleaning.R
import com.example.thoughts_cleaning.api.Prefs
import com.example.thoughts_cleaning.common.ErrorCodeApp
import com.example.thoughts_cleaning.common.util.showToast
import com.example.thoughts_cleaning.common.vm.BaseViewModel
import com.example.thoughts_cleaning.common.vm.SuniException
import com.example.thoughts_cleaning.views.start.view.activity.container.StartActivity
import kotlin.jvm.java
import kotlin.let

/**
 *
 */
abstract class MasilLeadActivity<T: ViewDataBinding, S: BaseViewModel>(@LayoutRes layoutId: Int) : BaseLeadActivity<T, S>(layoutId) {
    override val appLang: String = ""
    override val appNameId: Int = R.string.app_name

    override val customAppEvent: Observer<ErrorCodeApp?>? = null
    override val customServerEvent: Observer<Exception>? = Observer<Exception> {
        when (it) {
            is SuniException.InvalidLanguageException,
            is SuniException.ExpiredSessionException,
            is SuniException.InvalidSessionException -> {
//                Prefs.initUserInfo()
                val intent = Intent(this, StartActivity::class.java)
                goView(intent, true)
            }
            else -> processGeneralException(it)
        }
    }

    private fun showMessage(exception: Exception) {
        exception.message?.let { msg -> showToast(msg) }
    }

    open fun processGeneralException(exception: Exception) {
        showMessage(exception)
    }
}