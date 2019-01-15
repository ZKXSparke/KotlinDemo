package com.demo.barry.demoPage.fragment

import android.view.View
import com.demo.barry.demoPage.R
import com.demo.barry.base_utils.utils.Paths
import com.demo.barry.base_utils.utils.goActivity
import com.demo.barry.base_utils.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_building.*

class BuildingFragment : BaseFragment() {
    override fun initWidgets() {
    }

    override fun setListeners() {
        click(btn_thread,btn_event)
    }

    override fun onWidgetsClick(v: View) {
        when (v) {
            btn_event -> activity?.goActivity(Paths.DemoPage.EventBusActivity)
            btn_thread -> activity?.goActivity(Paths.DemoPage.ThreadPoolActivity)
        }
    }

    override fun bindLayout() = R.layout.fragment_building

    override fun useTitleBar() = false
}