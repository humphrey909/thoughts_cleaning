package com.example.thoughts_cleaning.util.dialog.recent

import android.content.Context
import android.util.Log
import com.example.thoughts_cleaning.R
import kr.dnx.ble.android.touchcare.library.views.custom.dialog.recent.CommonDialogType

/**
 * Created by SeoKang on 2023-02-20.
 * Created by Humphrey on 2024-07-09.
 */

class CommonDialogBuilder(val context: Context, val type: CommonDialogType) {
    var onConfirmListener: (() -> Unit)? = null
    var onCancelListener: (() -> Unit)? = null

    var imgUrl: String = ""

    var title: String = ""
    var titleSub: String = ""
    var main: String = ""

    var confirmText: String = context.getString(R.string.dialog_button_confirm)
    var cancelText: String = context.getString(R.string.dialog_button_cancel)

    fun build() = CommonDialog(this)

    fun onConfirmListener(onConfirmListener: (() -> Unit)?): CommonDialogBuilder {
        this.onConfirmListener = onConfirmListener
        return this
    }

    fun onCancelListener(onCancelListener: () -> Unit): CommonDialogBuilder {
        this.onCancelListener = onCancelListener
        return this
    }

    fun imgUrl(imgUrl: String): CommonDialogBuilder {
        this.imgUrl = imgUrl
        Log.d("TAG", "setUrlImage 1 : $imgUrl")

        return this
    }

    fun title(title: String): CommonDialogBuilder {
        this.title = title
        return this
    }

    fun titleSub(titleSub: String): CommonDialogBuilder {
        this.titleSub = titleSub
        return this
    }

    fun main(main: String): CommonDialogBuilder {
        this.main = main
        return this
    }

    fun confirmText(confirmText: String): CommonDialogBuilder {
        this.confirmText = confirmText
        return this
    }

    fun cancelText(cancelText: String): CommonDialogBuilder {
        this.cancelText = cancelText
        return this
    }
}