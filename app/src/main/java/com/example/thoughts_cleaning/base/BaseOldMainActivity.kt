package com.example.thoughts_cleaning.base

import android.content.Intent
import android.os.Bundle
import androidx.annotation.OptIn
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.whenCreated
import androidx.media3.common.util.UnstableApi
import com.example.thoughts_cleaning.common.Constants
import com.example.thoughts_cleaning.event.EventMessage
import com.example.thoughts_cleaning.util.dialog.CustomDialogBuilder
import com.example.thoughts_cleaning.util.dialog.DialogColorType

/**
 * Created by SeoKang on 2021-05-28.
 */
abstract class BaseOldMainActivity : BaseOldNavigationActivity()  {
//    init {
//        lifecycleScope.launch {
//            whenCreated {
//                EventBus.subscribe<EventMessage>().collect { message ->
//                    receiveEventPush(message)
//                }
//            }
//        }
//    }

    abstract fun checkUserId()
    abstract fun goLoginView()

    @OptIn(UnstableApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        intent.extras?.let { bundle ->
            val data = bundle.getSerializable(Constants.KEY_EVENT_PUSH) as? EventMessage
            data?.let { receiveEventPush(it) }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        intent?.let { intent ->
            intent.extras?.let { bundle ->
                val push = bundle.getSerializable(Constants.KEY_EVENT_PUSH) as? EventMessage
                push?.let { receiveEventPush(it) }
            }
        }

    }

    override fun onStart() {
        super.onStart()
        checkUserId()

    }

    @OptIn(UnstableApi::class)
    private fun receiveEventPush(push: EventMessage) {
        when (push.eventType) {
            Constants.PUSH_EVENT_AUTO_LOGOUT -> {
                val dialog = CustomDialogBuilder(this)
                    .title(push.title)
                    .body(push.body)
                    .onConfirmListener {
                        goLoginView()
                    }
                    .build()

                showDialog(dialog)
            }

            Constants.PUSH_EVENT_ALERT_MESSAGE,
            Constants.PUSH_EVENT_MESSAGE -> {
                val dialog = CustomDialogBuilder(this)
                    .title(push.title)
                    .body(push.body)
                    .color(DialogColorType.RED)
                    .build()

                showDialog(dialog)
            }
        }
    }
}