package com.demo.barry.demoPage.demoActivity.eventBus

import android.view.View
import com.demo.barry.base_utils.base.BaseActivity
import com.demo.barry.base_utils.base.BasePresenter
import com.demo.barry.base_utils.utils.Paths
import com.demo.barry.demoPage.R
import kotlinx.android.synthetic.main.activity_eventbus.*
import org.greenrobot.eventbus.EventBus
import pers.victor.smartgo.Path

/**
 * Created by Barry.Zhou on 2019/1/15
 */

@Path(Paths.DemoPage.SecondActivity)
class SecondActivity :BaseActivity<BasePresenter>(){
    override fun initWidgets() {
        tv_message.text = getString(R.string.SecondActivity)
        btn_subscription.text = getString(R.string.sendStickyEvents)
        btn_message.text = getString(R.string.sendEvent)
    }

    override fun setListeners() {
        click(btn_message,btn_subscription)
    }

    override fun onWidgetsClick(v: View) {
        when(v){
            btn_message ->{
                EventBus.getDefault().post(MessageEvent(getString(R.string.wish)))
                finish()
            }
            btn_subscription ->{
                EventBus.getDefault().postSticky(MessageEvent(getString(R.string.stickyEvents)))
                finish()
            }
        }
    }

    override fun bindLayout() = R.layout.activity_eventbus

}