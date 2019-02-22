package com.demo.barry.app_account.fragment

import android.view.View
import com.demo.barry.app_account.R
import com.demo.barry.base_utils.base.BaseFragment
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * Created by Barry.Zhou on 2019/1/18
 */

class IntroductionFragment : BaseFragment(){

    private var isVisibleToUser = true

    override fun initWidgets() {
        EventBus.getDefault().register(this)
    }

    override fun setListeners() {
    }

    override fun onWidgetsClick(v: View) {
   }

    override fun bindLayout() = R.layout.fragment_introduction

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun refresh(isVisibleToUser : Boolean){
        this.isVisibleToUser = isVisibleToUser
    }


    override fun onDestroy() {
        super.onDestroy()
        //取消注册事件
        EventBus.getDefault().unregister(this)
    }

}