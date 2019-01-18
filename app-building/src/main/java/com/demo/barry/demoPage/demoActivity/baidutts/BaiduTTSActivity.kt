package com.demo.barry.demoPage.demoActivity.baidutts

import android.Manifest
import android.content.pm.PackageManager
import android.media.AudioManager
import android.os.Handler
import android.os.Message
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.view.View
import com.baidu.tts.client.SpeechSynthesizer
import com.baidu.tts.client.TtsMode
import com.demo.barry.base_utils.base.BaseActivity
import com.demo.barry.base_utils.base.BasePresenter
import com.demo.barry.base_utils.utils.Paths
import com.demo.barry.demoPage.R
import kotlinx.android.synthetic.main.activity_baidutts.*
import pers.victor.smartgo.Path
import java.util.*

/**
 * Created by Barry.Zhou on 2019/1/18
 */

@Path(Paths.DemoPage.BaiduTTSActivity)
abstract class BaiduTTSActivity : BaseActivity<BasePresenter>(){

    private val appId = "11005757"

    private val appKey = "Ovcz19MGzIKoDDb3IsFFncG1"

    private val secretKey = "e72ebb6d43387fc7f85205ca7e6706e2"

    private val ttsMode : TtsMode = TtsMode.ONLINE

    private var offlineVoice = OfflineResource.VOICE_MALE

    private var synthesizer : MySyntherizer? = null

    protected var mSpeechSynthesizer: SpeechSynthesizer? = null

    protected abstract var mainHandler: android.os.Handler

    override fun initWidgets() {
        initPermission()
        initTTs()

    }

    override fun setListeners() {
        click(btn_speak)
    }

    override fun onWidgetsClick(v: View) {
        when(v) {
            btn_speak ->{
                speak()
            }
        }
    }

    private fun initTTs(){
        val isMix = ttsMode == TtsMode.ONLINE
        val isSuccess: Boolean
        val listener = UiMessageListener(mainHandler)
        mSpeechSynthesizer = SpeechSynthesizer.getInstance()
        mSpeechSynthesizer!!.setContext(this)
        mSpeechSynthesizer?.setSpeechSynthesizerListener(listener)
        var result = mSpeechSynthesizer!!.setAppId(appId)
        checkResult(result, "setAppId")
        result = mSpeechSynthesizer!!.setApiKey(appKey, secretKey)
        checkResult(result, "setApiKey")
        // 设置在线发声音人： 0 普通女声（默认） 1 普通男声 2 特别男声 3 情感男声<度逍遥> 4 情感儿童声<度丫丫>
        mSpeechSynthesizer?.setParam(SpeechSynthesizer.PARAM_SPEAKER, "0")
        // 设置合成的音量，0-9 ，默认 5
        mSpeechSynthesizer?.setParam(SpeechSynthesizer.PARAM_VOLUME, "9")
        // 设置合成的语速，0-9 ，默认 5
        mSpeechSynthesizer?.setParam(SpeechSynthesizer.PARAM_SPEED, "5")
        // 设置合成的语调，0-9 ，默认 5
        mSpeechSynthesizer?.setParam(SpeechSynthesizer.PARAM_PITCH, "5")

//        mSpeechSynthesizer?.setParam(SpeechSynthesizer.PARAM_MIX_MODE, SpeechSynthesizer.MIX_MODE_DEFAULT)
        mSpeechSynthesizer?.setAudioStreamType(AudioManager.MODE_IN_CALL)
        val params = HashMap<String, String>()
        val initConfig  = InitConfig(appId, appKey, secretKey, ttsMode, params, listener)
        AutoCheck.getInstance(applicationContext).check(initConfig, object : Handler() {
            override
                    /**
                     * 开新线程检查，成功后回调
                     */
            fun handleMessage(msg: Message) {
                if (msg.what == 100) {
                    val autoCheck = msg.obj as AutoCheck
                    synchronized(autoCheck) {
                        val message = autoCheck.obtainDebugMessage()
                        print(message) // 可以用下面一行替代，在logcat中查看代码
                        // Log.w("AutoCheckMessage", message);
                    }
                }
            }

        })

        result = mSpeechSynthesizer!!.initTts(ttsMode)
        checkResult(result, "initTts")
    }

    private fun checkResult(result: Int, method: String) {
        if (result != 0) {
            print("error code :$result method:$method, 错误码文档:http://yuyin.baidu.com/docs/tts/122 ")
        }
    }

    private fun speak() {
        /* 以下参数每次合成时都可以修改
         *  mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEAKER, "0");
         *  设置在线发声音人： 0 普通女声（默认） 1 普通男声 2 特别男声 3 情感男声<度逍遥> 4 情感儿童声<度丫丫>
         *  mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_VOLUME, "5"); 设置合成的音量，0-9 ，默认 5
         *  mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEED, "5"); 设置合成的语速，0-9 ，默认 5
         *  mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_PITCH, "5"); 设置合成的语调，0-9 ，默认 5
         *
         *  mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_MIX_MODE, SpeechSynthesizer.MIX_MODE_DEFAULT);
         *  MIX_MODE_DEFAULT 默认 ，wifi状态下使用在线，非wifi离线。在线状态下，请求超时6s自动转离线
         *  MIX_MODE_HIGH_SPEED_SYNTHESIZE_WIFI wifi状态下使用在线，非wifi离线。在线状态下， 请求超时1.2s自动转离线
         *  MIX_MODE_HIGH_SPEED_NETWORK ， 3G 4G wifi状态下使用在线，其它状态离线。在线状态下，请求超时1.2s自动转离线
         *  MIX_MODE_HIGH_SPEED_SYNTHESIZE, 2G 3G 4G wifi状态下使用在线，其它状态离线。在线状态下，请求超时1.2s自动转离线
         */

        if (mSpeechSynthesizer == null) {
            print("[ERROR], 初始化失败")
            return
        }
        val result = mSpeechSynthesizer!!.speak(et_ttsmsg.text.toString())
        print("合成并播放 按钮已经点击")
        checkResult(result, "speak")
    }

    override fun bindLayout() = R.layout.activity_baidutts

    override fun onStop() {
        super.onStop()
        val result = mSpeechSynthesizer!!.stop()
        checkResult(result, "stop")
    }

    override fun onDestroy() {
        if (mSpeechSynthesizer != null) {
            mSpeechSynthesizer!!.stop()
            mSpeechSynthesizer!!.release()
            mSpeechSynthesizer = null
            print("释放资源成功")
        }
        super.onDestroy()
    }

    /**
     * android 6.0 以上需要动态申请权限
     */
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

}