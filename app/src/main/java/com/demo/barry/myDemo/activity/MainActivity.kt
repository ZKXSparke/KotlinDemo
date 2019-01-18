package com.demo.barry.myDemo.activity

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.view.View
import com.demo.barry.app_account.fragment.MineFragment
import com.demo.barry.demoPage.fragment.BuildingFragment
import com.demo.barry.base_utils.utils.Paths
import com.demo.barry.myDemo.R
import com.demo.barry.myDemo.fragment.HomeFragment
import com.demo.barry.base_utils.base.BaseActivity
import com.demo.barry.base_utils.base.Nil
import kotlinx.android.synthetic.main.activity_main.*
import pers.victor.ext.*
import pers.victor.smartgo.Path
import java.util.ArrayList

@Path(Paths.App.MainActivity)
class MainActivity : BaseActivity<Nil>() {

    private val fragments by lazy { arrayListOf(HomeFragment(), BuildingFragment(), MineFragment()) }

    override fun initData() {
        addFragments(fragments, R.id.ff_fragment)
        fragments.forEachIndexed { i, f -> if (i == 0) showFragment(f) else hideFragment(f) }
    }

    override fun initWidgets() {
        window.setBackgroundDrawable(ColorDrawable(Color.WHITE))
        initPermission()
        initBottomNavigation()
    }

    override fun setListeners() {
    }

    override fun onWidgetsClick(v: View) {
    }

    override fun bindLayout() = R.layout.activity_main

    private fun initBottomNavigation() {
        val tabs = ll_main_bottom.children.apply { get(0).isSelected = true }
        tabs.forEachIndexed { i, v ->
            v.click { _ ->
                fragments.forEachIndexed { j, f ->
                    if (i == j) showFragment(f) else hideFragment(f)
                    tabs.forEach {
                        it.isSelected = it == v
                    }
                }
            }
        }
    }

    private fun initPermission() {
        val permissions = arrayOf(Manifest.permission.INTERNET, Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.MODIFY_AUDIO_SETTINGS, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.WRITE_SETTINGS, Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_WIFI_STATE, Manifest.permission.CHANGE_WIFI_STATE)

        val toApplyList = ArrayList<String>()

        for (perm in permissions) {
            if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this, perm)) {
                toApplyList.add(perm)
                // 进入到这里代表没有权限.
            }
        }
        val tmpList = arrayOfNulls<String>(toApplyList.size)
        if (!toApplyList.isEmpty()) {
            ActivityCompat.requestPermissions(this, toApplyList.toTypedArray(), 123)
        }

    }

    override fun useTitleBar() = false

    override fun invasionStatusBar() = true

}
