package com.example.thoughts_cleaning.base

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.thoughts_cleaning.common.ErrorCodeApp
import com.example.thoughts_cleaning.common.util.showToast
import com.example.thoughts_cleaning.common.vm.BaseViewModel
import com.example.thoughts_cleaning.util.base.BaseContract
import com.example.thoughts_cleaning.util.dialog.CustomDialog
import com.example.thoughts_cleaning.util.dialog.recent.CommonDialog

/**
 * Created by SeoKang on 2021-05-20.
 */
abstract class BaseFragment<T: ViewDataBinding, S: BaseViewModel>(
    @LayoutRes override val layoutId: Int,
    override var _binding: T? = null
) : Fragment(layoutId), BaseFragmentComponent<T, S>, BaseObserveComponent {
    override val binding get() = _binding!!
    override val defaultAppEvent = Observer<ErrorCodeApp?> { code ->
        code?.let { showErrorToast(it) } ?:
        run { showNetworkDialog { activity?.finish() } }
    }
    override val defaultServerEvent = Observer<Exception> { it.message?.let { msg -> context?.showToast(msg) }  }

    lateinit var navigationMethod: BaseContract.NavMethod
    override fun onAttach(context: Context) {
        super.onAttach(context)

        navigationMethod = requireActivity() as BaseContract.NavMethod
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = onCreateViewImpl(inflater, container)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = this
    }

    override fun onStop() {
        super.onStop()

        this.hideProgress()
        this.hideDialog()
    }

    override fun setObserveLiveData() {
        val serverEvent = customServerEvent ?: defaultServerEvent
        val appEvent = customAppEvent ?: defaultAppEvent

        viewModel.showServerEvent.observe(viewLifecycleOwner, serverEvent)
        viewModel.showAppEvent.observe(viewLifecycleOwner, appEvent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        onDestroyViewImpl()
    }

    override fun showNetworkDialog(onConfirmListener:(() -> Unit)?) = navigationMethod.showNetworkDialog(onConfirmListener)
    override fun showMessageToast(message: String) = navigationMethod.showMessageToast(message)
    override fun showErrorToast(errorCode: ErrorCodeApp) = navigationMethod.showErrorToast(errorCode)
    override fun goView(intent: Intent, isFinish: Boolean) = navigationMethod.goView(intent, isFinish)
    override fun showKeyboard(view: View) = navigationMethod.showKeyboard(view)
    override fun hideKeyboard(view: View?) = navigationMethod.hideKeyboard()

    protected fun showProgress() = navigationMethod.showProgress()
    protected fun hideProgress() = navigationMethod.hideProgress()
    protected fun showDialog(dialog: CommonDialog) = navigationMethod.showDialog(dialog)
    protected fun showDialog(dialog: CustomDialog) = navigationMethod.showDialog(dialog)
    protected fun hideDialog() = navigationMethod.hideDialog()
}