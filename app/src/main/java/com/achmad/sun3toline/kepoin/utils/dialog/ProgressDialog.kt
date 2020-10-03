/*
 * *
 *  * Created by Achmad Fathullah on 10/3/20 9:21 PM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 10/3/20 9:19 PM
 *
 */

package com.achmad.sun3toline.kepoin.utils.dialog

import android.app.Dialog
import android.content.Context
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.achmad.sun3toline.kepoin.R
import com.achmad.sun3toline.kepoin.utils.extension.lifecycleOwner
import kotlinx.android.synthetic.main.progress_dialog.*

class ProgressDialog(context: Context) : Dialog(context) {
    init {
        window?.setBackgroundDrawableResource(android.R.color.transparent)
        setContentView(R.layout.progress_dialog)
    }

    private inner class ModuleLifecycleObserver : LifecycleObserver {
        fun addObserver(lifecycle: Lifecycle) = lifecycle.addObserver(this)

        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        fun onDestroy() = dismiss()

        @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
        fun onPause() = dismiss()
    }

    private fun addLifecycleObserver(lifecycle: Lifecycle) {
        ModuleLifecycleObserver().addObserver(lifecycle)
    }

    private fun show(isShown: Boolean, isCancelable: Boolean = false) {
        if (!super.isShowing()) {
            setCancelable(isCancelable)
            context.lifecycleOwner()?.lifecycle?.let { addLifecycleObserver(it) }
            if (isShown) super.show()
            else super.dismiss()
        } else {
            super.dismiss()
        }
    }

    fun setMessage(text: String) {
        tv_progress_dialog?.text = text
    }

    override fun show() {
        show(true)
    }
}