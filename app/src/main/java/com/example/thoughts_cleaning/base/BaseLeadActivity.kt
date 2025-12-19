package com.example.thoughts_cleaning.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import com.example.thoughts_cleaning.common.ErrorCodeApp
import com.example.thoughts_cleaning.common.util.showToast
import com.example.thoughts_cleaning.common.vm.BaseViewModel

/**
 *
 */
abstract class BaseLeadActivity<T: ViewDataBinding, S: BaseViewModel>(
    @LayoutRes val layoutId: Int,
    private var _binding: T? = null
) : BaseActivity(), BaseObserveComponent {
    protected val binding get() = _binding!!

    protected abstract val viewModel: S

    override val defaultAppEvent = Observer<ErrorCodeApp?> { code ->
        code?.let { showErrorToast(it) } ?:
        run { showNetworkDialog { this@BaseLeadActivity.finish() } }
    }

    override val defaultServerEvent = Observer<Exception> { it.message?.let { msg -> showToast(msg) }  }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this, layoutId)
        binding.lifecycleOwner = this
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun setObserveLiveData() {
        val serverEvent = customServerEvent ?: defaultServerEvent
        val appEvent = customAppEvent ?: defaultAppEvent

        viewModel.showServerEvent.observe(this@BaseLeadActivity, serverEvent)
        viewModel.showAppEvent.observe(this@BaseLeadActivity, appEvent)
    }
}