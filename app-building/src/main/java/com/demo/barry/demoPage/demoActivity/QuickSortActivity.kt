package com.demo.barry.demoPage.demoActivity

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.demo.barry.base_utils.base.BaseActivity
import com.demo.barry.base_utils.base.BasePresenter
import com.demo.barry.base_utils.utils.Paths
import com.demo.barry.demoPage.R
import kotlinx.android.synthetic.main.activity_quicksort.*
import pers.victor.smartgo.Path


/**
 * Created by Barry.Zhou on 2019/2/15
 *
 * 快速排序算法
 */
@Path(Paths.DemoPage.QuickSortActivity)
class QuickSortActivity : BaseActivity<BasePresenter>(), TextWatcher {
    override fun afterTextChanged(s: Editable?) {
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }

    override fun initWidgets() {
    }

    override fun setListeners() {
        click(btn_quickSort)
        et_input.addTextChangedListener(this)
    }

    override fun onWidgetsClick(v: View) {
        when (v) {
            btn_quickSort -> {
                quick(et_input.text.toString())
            }
        }
    }

    override fun bindLayout() = R.layout.activity_quicksort

    private fun partition(array: IntArray, left: Int, right: Int): Int {
        //第一个为基准元素
        val baseElement = array[left]
        var nextElement = left + 1
        var lastElement = right
        while (true) {
            while (nextElement <= right && array[nextElement] < baseElement) nextElement++
            while (lastElement > left && array[lastElement] > baseElement) lastElement--
            //循环终止条件
            if (nextElement > lastElement) break
            // 交换 array[nextElement]与array[lastElement]
            val tag = array[nextElement]
            array[nextElement] = array[lastElement]
            array[lastElement] = tag
            nextElement++
            lastElement--
        }
        //将基准元素与array[nextElement]交换
        val tag = array[left]
        array[left] = array[lastElement]
        array[lastElement] = tag

        //返回基准元素所在位置
        return lastElement
    }

    private fun quickSort(array: IntArray, left: Int, right: Int) {
        if (left >= right) {
            return
        }
        //进行第一轮排序获取分割点
        val index = partition(array, left, right)
        //排序前半部分
        quickSort(array, left, index - 1)
        //排序后半部分
        quickSort(array, index + 1, right)
        tv_result.text = array.toString()
    }

    private fun quick(array: String) {
        val arr = IntArray(array.length)
        for (i in 0 until array.length) {
            arr[i] = Integer.parseInt(array.substring(i, i + 1))
        }
        if (array.isNotEmpty()) {
            quickSort(arr, 0, arr.size - 1)
        }
    }


}