package com.demo.barry.myDemo.fragment

import android.view.View
import com.demo.barry.base_utils.utils.GlideImageLoader
import com.demo.barry.myDemo.BANNER_ONE
import com.demo.barry.myDemo.BANNER_THREE
import com.demo.barry.myDemo.BANNER_TWO
import com.demo.barry.myDemo.R
import com.demo.barry.base_utils.base.BaseFragment
import com.youth.banner.BannerConfig
import com.youth.banner.BannerConfig.*
import com.youth.banner.Transformer
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : BaseFragment() {
    override fun initWidgets() {

        b_view.setImageLoader(GlideImageLoader())
        b_view.setImages(listOf(BANNER_ONE, BANNER_TWO, BANNER_THREE))
        b_view.setBannerStyle(CIRCLE_INDICATOR)
        b_view.setViewPagerIsScroll(true)
        b_view.setBannerAnimation(Transformer.Default)
        b_view.setDelayTime(2000)
        b_view.setIndicatorGravity(BannerConfig.RIGHT)
        b_view.start()
    }

    override fun setListeners() {
    }

    override fun onWidgetsClick(v: View) {
    }

    override fun bindLayout() = R.layout.fragment_home

    override fun useTitleBar() = false

}