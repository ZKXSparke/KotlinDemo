package com.demo.barry.myDemo.activity

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
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

@Path(Paths.App.MainActivity)
class MainActivity : BaseActivity<Nil>() {

    private val fragments by lazy { arrayListOf(HomeFragment(), BuildingFragment(), MineFragment()) }

    override fun initData() {
        addFragments(fragments, R.id.ff_fragment)
        fragments.forEachIndexed { i, f -> if (i == 0) showFragment(f) else hideFragment(f) }
    }

    override fun initWidgets() {
        window.setBackgroundDrawable(ColorDrawable(Color.WHITE))
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

    override fun useTitleBar() = false

    override fun invasionStatusBar() = true

}
