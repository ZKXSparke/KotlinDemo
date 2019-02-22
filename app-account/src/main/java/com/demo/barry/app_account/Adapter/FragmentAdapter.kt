package com.demo.barry.app_account.Adapter

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.view.ViewGroup

/**
 * Created by Barry.Zhou on 2019/1/18
 */

class FragmentAdapter(fm: FragmentManager,  val mContext: Context?) : FragmentPagerAdapter(fm) {

    private var titles: Array<String>? = null
    private var mList: MutableList<String>? = null
    private var mFragmentList: List<Fragment>? = null

    private var titleType = 0// 0：表示数组  1：表示集合

    // 设置 tab 标题
    fun setTitles(stringList: MutableList<String>) {
        this.mList = stringList
    }

    // 设置 Fragment
    fun setFragments(fragments: List<Fragment>) {
        this.mFragmentList = fragments
    }

    fun setTitles(tabTitles: Array<String>) {
        this.titles = tabTitles
    }

    fun setTabTilteType(type: Int) {
        this.titleType = type
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val fragment: Fragment?
        fragment = super.instantiateItem(container, position) as Fragment
        return fragment
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return if (titleType == 0) {
            titles!![position]
        } else {
            mList!![position]
        }
    }

    override fun getItem(position: Int): Fragment {
        return mFragmentList!![position]
    }

    override fun getCount(): Int {
        return if (titleType == 0) {
            titles!!.size
        } else {
            mList!!.size
        }
    }

    fun setPageTitle(position: Int, title: String) {
        if (position >= 0 && position < mList!!.size) {
            mList!![position] = title
            notifyDataSetChanged()
        }
    }

}