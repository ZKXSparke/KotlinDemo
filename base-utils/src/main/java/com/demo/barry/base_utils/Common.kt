package com.demo.barry.base_utils

import android.app.Application
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import pers.victor.ext.Ext

object Common {
    lateinit var context: Application

    fun with(app: Application) {
        this.context = app
        Ext.with(app)
        val formatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(true)
                .methodCount(0)
                .methodOffset(7)
                .tag("Common")
                .build()
        Logger.addLogAdapter(AndroidLogAdapter(formatStrategy))
    }
}