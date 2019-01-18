package com.demo.barry.demoPage.demoActivity.baidutts

import android.content.Context
import android.os.Build
import android.os.Handler
import android.os.HandlerThread
import android.os.Message

/**
 * Created by Barry.Zhou on 2019/1/18
 */

class BlockSyntherizer : MySyntherizer() {
    private val INIT = 1

    private val RELEASE = 11
    private var hThread: HandlerThread? = null
    private var tHandler: Handler? = null

    fun NonBlockSyntherizer(context: Context, initInitConfig: InitConfig, mainHandler: Handler) {
        super.MySyntherizer(context, mainHandler)
        initThread()
        runInHandlerThread(INIT, initInitConfig)
    }

    protected fun initThread(){
        hThread = HandlerThread("NonBlockSyntherizer-thread")
        hThread?.start()
        tHandler = object : Handler(hThread?.looper) {
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                when (msg.what) {
                    INIT -> {
                        val config = msg.obj as InitConfig
                        val isSuccess = init(config)
                        if (isSuccess) {
                            // speak("初始化成功");
                            sendToUiThread("NonBlockSyntherizer 初始化成功")
                        } else {
                            sendToUiThread("合成引擎初始化失败, 请查看日志")
                        }
                    }
                    RELEASE -> {
                        super@BlockSyntherizer.release()
                        if (Build.VERSION.SDK_INT < 18) {
                            looper.quit()
                        }
                    }
                    else -> {
                    }
                }

            }
        }
    }

    override fun release() {
        runInHandlerThread(RELEASE)
        if (Build.VERSION.SDK_INT >= 18) {
            hThread?.quitSafely()
        }
    }

    private fun runInHandlerThread(action: Int) {
        runInHandlerThread(action, null)
    }

    private fun runInHandlerThread(action: Int, obj: Any?) {
        val msg = Message.obtain()
        msg.what = action
        msg.obj = obj
        tHandler?.sendMessage(msg)
    }

}