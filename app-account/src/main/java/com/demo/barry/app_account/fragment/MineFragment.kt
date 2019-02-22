package com.demo.barry.app_account.fragment

import android.content.Context
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.view.View
import android.widget.LinearLayout
import com.demo.barry.app_account.Adapter.FragmentAdapter
import com.demo.barry.app_account.R
import com.demo.barry.base_utils.base.BaseFragment
import com.demo.barry.base_utils.utils.DensityUtil
import kotlinx.android.synthetic.main.fragment_mine.*
import org.greenrobot.eventbus.EventBus
import java.lang.reflect.Field

class MineFragment : BaseFragment(){

    private val mAdapter by lazy {
        FragmentAdapter(childFragmentManager,activity)
    }

    private var mListener: TabLayout.TabLayoutOnPageChangeListener? = null

    private val mTitle by lazy {
        arrayOf("分类","简介","我")
    }

    private val mFragments by lazy {
        listOf<Fragment>(IntroductionFragment(),ClassifyFragment(),OwnFragment())
    }


    override fun initWidgets() {
        mAdapter.setTabTilteType(0)
        mAdapter.setTitles(mTitle)
        mAdapter.setFragments(mFragments)
        vp_mine.offscreenPageLimit = 3
        vp_mine.adapter = mAdapter

        tl_mine.setupWithViewPager(vp_mine)

        mListener = TabLayout.TabLayoutOnPageChangeListener(tl_mine)
        mListener?.apply {
            vp_mine.addOnPageChangeListener(this)
        }
        modifyTabIndicatorStyle(activity, tl_mine, 25f, 25f)
    }

    override fun setListeners() {
    }

    override fun onWidgetsClick(v: View) {
    }

    override fun bindLayout() = R.layout.fragment_mine

    override fun useTitleBar() = false

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        EventBus.getDefault().post(isVisibleToUser)
    }

    private fun modifyTabIndicatorStyle(context: Context?, tabLayout: TabLayout, mergeStart: Float, mergeEnd: Float) {
        val tab = tabLayout.javaClass
        var tabStrip: Field? = null
        try {
            tabStrip = tab.getDeclaredField("mTabStrip")
            tabStrip!!.isAccessible = true
            val ll_tab = tabStrip.get(tabLayout) as LinearLayout
            for (i in 0 until ll_tab.childCount) {
                val child = ll_tab.getChildAt(i)
                child.setPadding(0, 0, 0, 0)
                val params = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1f)
                params.marginStart = DensityUtil.dip2px(context!!, mergeStart)
                params.marginEnd = DensityUtil.dip2px(context, mergeEnd)
                child.layoutParams = params
                child.invalidate()
            }
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }

    }

}