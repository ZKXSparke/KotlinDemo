package com.demo.barry.base_utils.view

import android.content.Context
import android.graphics.Color
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView

class AutoRecyclerView(context: Context, attrs: AttributeSet) : RecyclerView(context, attrs) {
    private val mHeaderViews = mutableListOf<View>()
    private val mFooterViews = mutableListOf<View>()
    private lateinit var mAdapter: Adapter<ViewHolder>
    private var mLoadDataListener: (() -> Unit)? = null
    private var isLoadingData = false
    private var isDefaultFooter = true
    private var emptyRes = 0

    init {
        overScrollMode = View.OVER_SCROLL_NEVER
        post {
            if (layoutManager is GridLayoutManager) {
                layoutGridAttach()
            } else if (layoutManager is StaggeredGridLayoutManager) {
                layoutStaggeredGridHeadAttach()
            }
        }
    }


    /**
     * 添加Header
     */
    fun addHeaderView(view: View) {
        if (view.layoutParams == null) {
            view.layoutParams = ViewGroup.LayoutParams(-1, -2)
        }
        mHeaderViews.clear()
        mHeaderViews.add(view)
    }


    /**
     * 添加Footer
     */
    fun addFootView(view: View) {
        if (view.layoutParams == null) {
            view.layoutParams = ViewGroup.LayoutParams(-1, -2)
        }
        mFooterViews.clear()
        mFooterViews.add(view)
        isDefaultFooter = false
        hasNextPage(false)
    }

    /**
     * 添加无数据时的占位图, 必须是defaultFooter时生效
     */
    fun setEmptyResource(resId: Int) {
        emptyRes = resId
    }

    fun hasHeader() = mHeaderViews.isNotEmpty()

    /**
     * 设置加载更多数据的监听
     */

    fun setOnLoadingListener(listener: (() -> Unit)) {
        mLoadDataListener = listener
    }

    /**
     * 加载更多数据完成后调用，必须在UI线程中
     */
    fun hasNextPage(hasNextPage: Boolean) {
        isLoadingData = false

        if (!isDefaultFooter) {
            postDelayed({ mFooterViews.forEach { it.visibility = View.VISIBLE } }, 333)
            return
        }

        if (mFooterViews.size > 0) {
            val defaultFooter = (mFooterViews[0] as ViewGroup)
            defaultFooter.visibility = View.INVISIBLE
            if (!hasNextPage) {
                //没有内容, 显示EmptyView
                if (mAdapter.itemCount - mHeaderViews.size - mFooterViews.size == 0 && emptyRes != 0) {
                    defaultFooter.visibility = View.VISIBLE
                    defaultFooter.getChildAt(0).visibility = View.GONE
                    defaultFooter.getChildAt(1).visibility = View.GONE
                    defaultFooter.getChildAt(2).visibility = View.VISIBLE
                } else {
                    //不加延时的话 "暂无更多"会立即显示出来
                    postDelayed({
                        defaultFooter.visibility = View.VISIBLE
                        defaultFooter.getChildAt(0).visibility = View.GONE
                        defaultFooter.getChildAt(1).visibility = View.VISIBLE
                        defaultFooter.getChildAt(2).visibility = View.GONE
                    }, 233)
                }
                mLoadDataListener = null
            } else {
                defaultFooter.getChildAt(0).visibility = View.VISIBLE
                defaultFooter.getChildAt(1).visibility = View.GONE
                defaultFooter.getChildAt(2).visibility = View.GONE
                defaultFooter.visibility = View.INVISIBLE
            }
        }
    }

    override fun setAdapter(adapter: Adapter<ViewHolder>) {
        if (mFooterViews.isEmpty()) {
            mFooterViews.add(createDefaultLoadMoreFooter())
        }
        mAdapter = WrapAdapter(adapter)
        super.setAdapter(mAdapter)
    }

    private fun layoutGridAttach() {
        val manager = layoutManager as GridLayoutManager
        val originLookUp = manager.spanSizeLookup
        manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if ((mAdapter as WrapAdapter).isHeader(position) || (mAdapter as WrapAdapter).isFooter(position))
                    manager.spanCount
                else
                    originLookUp.getSpanSize(position)
            }
        }
        requestLayout()
    }

    private fun layoutStaggeredGridHeadAttach() {
        for (i in 0 until mAdapter.itemCount) {
            if ((mAdapter as WrapAdapter).isHeader(i)) {
                val view = getChildAt(i)
                (view.layoutParams as StaggeredGridLayoutManager.LayoutParams).isFullSpan = true
                view.requestLayout()
            } else {
                break
            }
        }
    }


    override fun onScrollStateChanged(state: Int) {
        // 当前不滚动，且不是正在刷新或加载数据
        if (state == SCROLL_STATE_IDLE && mLoadDataListener != null && !isLoadingData) {
            val layoutManager = layoutManager
            val lastVisibleItemPosition: Int
            // 获取最后一个正在显示的Item的位置
            lastVisibleItemPosition = when (layoutManager) {
                is StaggeredGridLayoutManager -> {
                    val into = IntArray(layoutManager.spanCount)
                    layoutManager.findLastVisibleItemPositions(into)
                    findMax(into)
                }
                is GridLayoutManager -> layoutManager.findLastVisibleItemPosition()
                else -> (layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
            }

            if (layoutManager.childCount > 0 && lastVisibleItemPosition >= layoutManager.itemCount - 1) {
                if (mFooterViews.size > 0) {
                    mFooterViews[0].visibility = View.VISIBLE
                }
                // 加载更多
                isLoadingData = true
                mLoadDataListener!!.invoke()
            }
        }
    }

    private fun findMax(lastPositions: IntArray) = lastPositions.max() ?: lastPositions[0]

    /**
     * 自定义带有头部/脚部的适配器
     */
    private inner class WrapAdapter(private val originAdapter: Adapter<ViewHolder>) : Adapter<ViewHolder>() {
        private var headerPosition = 0

        /**
         * 当前布局是否为Header
         */
        fun isHeader(position: Int) = position >= 0 && position < mHeaderViews.size

        /**
         * 当前布局是否为Footer
         */
        fun isFooter(position: Int) = position < itemCount && position >= itemCount - mFooterViews.size

        override fun getItemViewType(position: Int): Int {
            if (isHeader(position)) {
                return INVALID_TYPE
            }
            val adjPosition = position - headerPosition
            val adapterCount = originAdapter.itemCount
            if (adjPosition < adapterCount) {
                return originAdapter.getItemViewType(adjPosition)
            }
            return INVALID_TYPE - 1
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            if (viewType == INVALID_TYPE) {
                return HeaderViewHolder(mHeaderViews[headerPosition++])
            } else if (viewType == INVALID_TYPE - 1) {
                val params = StaggeredGridLayoutManager.LayoutParams(
                        StaggeredGridLayoutManager.LayoutParams.MATCH_PARENT,
                        StaggeredGridLayoutManager.LayoutParams.WRAP_CONTENT)
                params.isFullSpan = true
                mFooterViews[0].layoutParams = params
                return HeaderViewHolder(mFooterViews[0])
            }
            return originAdapter.onCreateViewHolder(parent, viewType)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            if (isHeader(holder.adapterPosition)) {
                return
            }
            val adjPosition = holder.adapterPosition - mHeaderViews.size
            val adapterCount = originAdapter.itemCount
            if (adjPosition < adapterCount) {
                originAdapter.onBindViewHolder(holder, adjPosition)
            }
        }

        override fun getItemCount() = mHeaderViews.size + mFooterViews.size + originAdapter.itemCount
    }

    private fun createDefaultLoadMoreFooter(): View {
        val height = dp2px(45)
        val linear = LinearLayout(context)
        linear.gravity = Gravity.CENTER
        linear.orientation = LinearLayout.HORIZONTAL
        val progressBar = ProgressBar(context)
        val padding = dp2px(10)
        progressBar.setPadding(padding, padding, padding, padding)
        linear.addView(progressBar, height, height)
        val text = TextView(context)
        text.gravity = Gravity.CENTER
        text.textSize = 13f
        text.text = "暂无更多"
        text.setTextColor(Color.parseColor("#808080"))
        text.visibility = View.GONE
        text.setPadding(0, 0, 0, dp2px(8))
        linear.addView(text, -1, height)
        val image = ImageView(context)
        image.setImageResource(emptyRes)
        image.visibility = View.GONE
        image.scaleType = ImageView.ScaleType.FIT_CENTER
        val paddingT = dp2px(100)
        image.setPadding(0, paddingT, 0, 0)
        val size = dp2px(120)
        linear.addView(image, size, size + paddingT)
        linear.visibility = View.INVISIBLE
        return linear
    }

    private fun dp2px(dp: Number) = (context.resources.displayMetrics.density * dp.toFloat() + 0.5f).toInt()

    class HeaderViewHolder(itemView: View) : ViewHolder(itemView)
}
