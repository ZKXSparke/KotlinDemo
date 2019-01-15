package com.demo.barry.myDemo

import android.app.Application
import com.demo.barry.base_utils.Common

class App : Application(){
    override fun onCreate() {
        super.onCreate()
        Common.with(this)
    }
}