package com.demo.barry.demoPage.demoActivity.eventBus

import android.view.View
import com.demo.barry.base_utils.utils.Paths
import com.demo.barry.demoPage.R
import com.demo.barry.base_utils.base.BaseActivity
import com.demo.barry.base_utils.base.BasePresenter
import com.demo.barry.base_utils.utils.goActivity
import com.demo.barry.base_utils.utils.showToast
import kotlinx.android.synthetic.main.activity_eventbus.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import pers.victor.smartgo.Path

@Path(Paths.DemoPage.EventBusActivity)
class EventBusActivity : BaseActivity<BasePresenter>(){
    val Tag = "EventBusActivity"
    override fun initWidgets() {
        tv_message.text = getString(R.string.EventBusActivity)
        btn_subscription.text = getString(R.string.subscriptionEvent)
        btn_message.text = getString(R.string.jumpToSecond)
    }

    override fun setListeners() {
        click(btn_message,btn_subscription)
    }

    override fun onWidgetsClick(v: View) {
        when(v){
            //RegisteredEvent
            btn_subscription ->{
                if (!EventBus.getDefault().isRegistered(this)){
                    EventBus.getDefault().register(this)
                }else{
                    showToast(getString(R.string.repeatRegistration))
                }
            }
            btn_message ->{
                goActivity(Paths.DemoPage.SecondActivity)
            }
        }
    }

    override fun bindLayout() = R.layout.activity_eventbus

    override fun onDestroy() {
        super.onDestroy()
        //取消注册事件
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMoonEvent(messageEvent: MessageEvent) {
        tv_message.text = messageEvent.getMessage()
    }

    @Subscribe(sticky = true)
    fun ononMoonStickyEvent(messageEvent: MessageEvent) {
        tv_message.text = messageEvent.getMessage()
    }
}