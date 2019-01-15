package com.demo.barry.base_utils.view

import android.app.Activity
import android.support.v7.app.AlertDialog
import com.centling.base.R
import kotlinx.android.synthetic.main.loading_dialog.view.*
import pers.victor.ext.inflate

class LoadingDialog(aty: Activity) : AlertDialog(aty, R.style.LoadingDialog) {
    private val view by lazy { inflate(R.layout.loading_dialog)}

    init {
        setView(view)
        ownerActivity = aty
    }

    override fun dismiss() {
        if (ownerActivity != null && !ownerActivity.isDestroyed) {
            super.dismiss()
        }
    }

    fun setText(message: CharSequence?) {
        view.tv_loading_dialog_title.text = message
    }

    fun show(message: CharSequence?) {
        view.tv_loading_dialog_title.text = message
        super.show()
        val lp = window.attributes
        lp.dimAmount = 0f
        onWindowAttributesChanged(lp)
    }
}