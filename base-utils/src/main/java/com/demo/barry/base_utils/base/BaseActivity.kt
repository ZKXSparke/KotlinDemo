package com.demo.barry.base_utils.base

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.*
import android.widget.LinearLayout
import com.centling.base.R
import com.demo.barry.base_utils.utils.isNetworkAvailable
import com.demo.barry.base_utils.utils.showToast
import com.demo.barry.base_utils.view.AutoRecyclerView
import com.demo.barry.base_utils.view.LoadingDialog
import com.jaeger.library.StatusBarUtil
import org.greenrobot.eventbus.EventBus
import pers.victor.ext.ActivityMgr
import pub.devrel.easypermissions.EasyPermissions

abstract class BaseActivity<T : BasePresenter> : AppCompatActivity(), View.OnClickListener, EasyPermissions.PermissionCallbacks {
    private lateinit var layoutMain: LinearLayout
    private lateinit var loadingDialog: LoadingDialog
    private lateinit var presenter: T
    var exitTime = 0L
    private var lastClickTime = 0L
    private var doubleBackFinish = false
    private var fakeBack = false
    private var registerEventBus = false
    private var loadDataAtFirst = true
    private var onPermissionsDenied: ((String) -> Unit)? = null
    private var onPermissionsGranted: ((String) -> Unit)? = null
    private var invasion = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadingDialog = LoadingDialog(this)
        if (savedInstanceState != null) {
            finishAffinity()
//            goActivity(Paths.App.SplashActivity)
            return
        }

        if (useTitleBar()) {
            layoutMain = LinearLayout(this)
            layoutMain.orientation = LinearLayout.VERTICAL
//            titleBar = TitleBar(this)
            //titleBar?.useElevation()
//            layoutMain.addView(titleBar, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
//            titleBar?.setLeftDrawable(R.drawable.ic_back)
//            titleBar?.setTitleBarLeftClick { finish() }
//            titleBar?.setTitleBarText(intent.getStringExtra("title")
//                    ?: getString(R.string.app_name))
        }
        if (allowFullScreen()) {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }
        setContentView(bindLayout())
        window.setBackgroundDrawable(ColorDrawable(Color.WHITE))
//        window.setBackgroundDrawable(ColorDrawable(findColor(R.color.background_grey)))
        ActivityMgr.add(this)
        initData()
        initWidgets()
        setListeners()
        if (registerEventBus) {
            EventBus.getDefault().register(this)
        }
        if (loadDataAtFirst) {
            loadData()
            loadData(true)
        }
        useStatusBar()
    }

    protected open fun useStatusBar() {
        StatusBarUtil.setLightMode(this)
        if (!invasionStatusBar()) {
            StatusBarUtil.setTranslucent(this, 1)
            StatusBarUtil.setColor(this, Color.WHITE, 0)
        } else {
            StatusBarUtil.setTransparentForImageViewInFragment(this, null)
        }
    }

    protected open fun useTitleBar() = true

    protected open fun invasionStatusBar() = false

    protected fun setPresenterImpl(presenter: T) {
        this.presenter = presenter
        if (presenter is Nil) {
            throw IllegalArgumentException("泛型Nil不能实例化")
        }
    }

    override fun setContentView(layoutResID: Int) {
        if (useTitleBar()) {
            val view = layoutInflater.inflate(layoutResID, layoutMain, false)
            layoutMain.addView(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
            super.setContentView(layoutMain)
        } else {
            super.setContentView(layoutResID)
        }
    }

    override fun setContentView(view: View) {
        if (useTitleBar()) {
            layoutMain.addView(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
            super.setContentView(layoutMain)
        } else {
            super.setContentView(view)
        }
    }

    override fun setContentView(view: View, params: ViewGroup.LayoutParams?) {
        if (useTitleBar()) {
            layoutMain.addView(view, params)
            super.setContentView(layoutMain)
        } else {
            super.setContentView(view, params)
        }
    }
//
//    protected fun setTitleBarBackground(id: Int) = titleBar?.setBackgroundColor(id)
//
//    protected fun getTitleBarRight() = titleBar?.getRightText()
//
//    protected fun getTitleBarLeft() = titleBar?.getLeftText()
//
//    protected fun setTitleBarText(title: String?) = titleBar?.setTitleBarText(title)
//
//    protected fun setTitleBarText(id: Int) = titleBar?.setTitleBarText(resources.getString(id))
//
//    protected fun setTitleBarRightColor(color: Int) = titleBar?.setTitleBarRightColor(findColor(color))
//
//    protected fun setTitleBarRightEnable(enable: Boolean) {
//        titleBar?.setTitleBarRightEnable(enable)
//    }
//
//    protected fun getTitleBarRightEnable(): Boolean {
//        return titleBar?.getTitleBarRightEnable() ?: false
//    }
//
//    protected fun setTitleBarColor(color: Int) = titleBar?.setTitleBarColor(findColor(color))
//
//    protected fun setTitleBarLeft(drawable: Int) = titleBar?.setLeftDrawable(drawable)
//
//    protected fun setTitleBarRight(drawable: Int) = titleBar?.setRightDrawable(drawable)
//
//    protected fun setTitleBarLeft(leftText: String) = titleBar?.setLeftText(leftText)
//
//    protected fun setTitleBarRight(rightText: String) = titleBar?.setRightText(rightText)
//
//    protected fun setTitleBarRight(rightText: String, iconId: Int, iconAtLeft: Boolean = true) = titleBar?.setRightTextAndIcon(rightText, iconId, iconAtLeft)
//
//    protected fun setTitleBarLeft(leftText: String, iconId: Int, iconAtLeft: Boolean = true) = titleBar?.setLeftTextAndIcon(leftText, iconId, iconAtLeft)
//
//    protected fun setTitleBarLeftVisibility(visibility: Int) = titleBar?.setLeftVisibility(visibility)
//
//    protected fun setTitleBarRightVisibility(visibility: Int) = titleBar?.setRightVisibility(visibility)
//
//    protected fun setTitleBarVisibility(visibility: Int) = { titleBar?.visibility = visibility }()
//
//    protected fun setTitleBarRightClick(func: () -> Unit) = titleBar?.setTitleBarRightClick { func() }
//
//    protected fun setTitleBarLeftClick(func: () -> Unit) = titleBar?.setTitleBarLeftClick { func() }
//
//    protected fun setTitleBarTitleClick(click: () -> Unit) = titleBar?.setTitleBarTitleClick { click() }
//
//    protected fun registerEventBus() = { this.registerEventBus = true }()
//
//    protected fun getTitleBar() = titleBar


    override fun onPause() {
        super.onPause()
        if (loadingDialog.isShowing) {
            dismissLoadingDialog()
        }
        // Bugtags.onPause(this)
    }

    override fun onDestroy() {
        if (registerEventBus) {
            EventBus.getDefault().unregister(this)
        }
        ActivityMgr.remove(this)
        onPermissionsGranted = null
        onPermissionsDenied = null
        super.onDestroy()
    }

    override fun onClick(v: View) {
        if (System.currentTimeMillis() - lastClickTime > 300) {
            lastClickTime = System.currentTimeMillis()
            onWidgetsClick(v)
        }
    }

    protected abstract fun initWidgets()

    protected abstract fun setListeners()

    protected abstract fun onWidgetsClick(v: View)

    protected abstract fun bindLayout(): Int

    protected open fun initData() {
    }

    protected open fun loadData() {
    }

    protected open fun loadData(isRefresh: Boolean) {
    }

    protected fun disallowLoadDataAtFirst() = { loadDataAtFirst = false }()

    protected fun click(vararg views: View) = views.forEach { it.setOnClickListener(this) }

    fun requestPermission(vararg permission: String, granted: ((String) -> Unit)? = null, denied: ((String) -> Unit)? = null, rationale: String? = null) {
        this.onPermissionsGranted = granted
        this.onPermissionsDenied = denied
        val list = arrayListOf<String>()
        permission.forEach {
            if (EasyPermissions.hasPermissions(this, it)) {
                this.onPermissionsGranted?.invoke(it)
            } else {
                list.add(it)
            }
        }
        EasyPermissions.requestPermissions(this, String.format(getString(R.string.need_permission), getString(R.string.app_name)), 110, *list.toTypedArray())
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsDenied(requestCode: Int, permissions: MutableList<String>) {
        permissions.forEach { this.onPermissionsDenied?.invoke(String.format(getString(R.string.need_permission), getString(R.string.app_name))) }
        this.onPermissionsDenied = null
    }

    override fun onPermissionsGranted(requestCode: Int, permissions: MutableList<String>) {
        permissions.forEach { this.onPermissionsGranted?.invoke(it) }
        this.onPermissionsGranted = null
    }

    protected open fun allowFullScreen() = false

    fun showLoadingDialog(msg: String = getString(R.string.loading), cancelable: Boolean = false) {
        if (loadingDialog.isShowing) {
            loadingDialog.setText(msg)
            return
        }
        loadingDialog.setCancelable(cancelable)
        loadingDialog.setCanceledOnTouchOutside(cancelable)
        loadingDialog.show(msg)
    }

    fun showLoadingDialog(cancelable: Boolean) = showLoadingDialog(getString(R.string.loading), cancelable)

    fun dismissLoadingDialog() = loadingDialog.dismiss()

    protected fun setDoubleBackFinish() = { this.doubleBackFinish = true }()

    protected fun setFakeBack() = { this.fakeBack = true }()

    override fun onBackPressed() {
        if (!doubleBackFinish) {
            if (fakeBack) moveTaskToBack(true) else super.onBackPressed()
            return
        }
        if (System.currentTimeMillis() - exitTime > 2000) {
            showToast(getString(R.string.click_again))
            exitTime = System.currentTimeMillis()
        } else {
            if (fakeBack) moveTaskToBack(true) else super.onBackPressed()
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun handleDataList(recyclerView: AutoRecyclerView?, oldList: MutableList<out Any>, newList: List<Any>, isRefresh: Boolean, hasNextPage: Boolean, loadDataListener: (() -> Unit)) {
        recyclerView?.let {
            oldList as MutableList<Any>
            if (isRefresh) {
                it.scrollToPosition(0)
                oldList.clear()
                oldList.addAll(newList)
                it.adapter.notifyDataSetChanged()
                it.setOnLoadingListener { loadDataListener() }
            } else {
                var size = oldList.size
                if (it.hasHeader()) {
                    size++
                }
                oldList.addAll(newList)
                it.adapter.notifyItemRangeInserted(size, newList.size)
            }
            it.hasNextPage(hasNextPage)
        }
    }

//    override fun attachBaseContext(newBase: Context?) {
//        if (languageType.isNotEmpty()) {
//            super.attachBaseContext(MultiLanguageUtil.attachBaseContext(newBase!!))
//        } else {
//            super.attachBaseContext(newBase!!)
//        }
//    }

    override fun onResume() {
        super.onResume()
        if (!isNetworkAvailable()) {
            showToast(getString(R.string.check_network))
        }
    }

}
