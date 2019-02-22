package com.demo.barry.demoPage.fragment

import android.Manifest
import android.os.Build
import android.support.v4.app.ActivityCompat
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
        click(btn_thread,btn_event,btn_tts,btn_baidutts)
    }

    override fun onWidgetsClick(v: View) {
        when (v) {
            btn_event -> activity?.goActivity(Paths.DemoPage.EventBusActivity)
            btn_thread -> activity?.goActivity(Paths.DemoPage.ThreadPoolActivity)
            btn_tts -> requestPermissions()
            btn_baidutts -> activity?.goActivity(Paths.DemoPage.BaiduTTSActivity)
            btn_quickSort -> activity?.goActivity(Paths.DemoPage.QuickSortActivity)
        }
    }

    override fun bindLayout() = R.layout.fragment_building

    private fun requestPermissions(){
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
//            val permissionList = arrayOf(Manifest.permission.WRITE_SETTINGS)
//            requestPermissions(permissionList,1 )
//        }else{
            activity?.goActivity(Paths.DemoPage.TTSActivity)
//        }
    }

    override fun useTitleBar() = false
}