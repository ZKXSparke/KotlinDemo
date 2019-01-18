package com.demo.barry.demoPage.demoActivity

import android.os.Bundle
import android.view.View
import com.demo.barry.base_utils.base.BaseActivity
import com.demo.barry.base_utils.base.BasePresenter
import com.demo.barry.base_utils.utils.Paths
import com.iflytek.cloud.*
import kotlinx.android.synthetic.main.activity_tts.*
import pers.victor.smartgo.Path


/**
 * Created by Barry.Zhou on 2019/1/17
 */

@Path(Paths.DemoPage.TTSActivity)
class TTSActivity : BaseActivity<BasePresenter>() ,InitListener, SynthesizerListener{
    var isInitTTS = false
    private val mTts = SpeechSynthesizer.createSynthesizer(this,null)

    override fun initWidgets() {
        SpeechUtility.createUtility(this,"appid=5c3fe414")
        Setting.setShowLog(true)
        initTTS()
    }

    override fun setListeners() {
        click(btn_speak)
    }

    override fun onWidgetsClick(v: View) {
        when(v){
            btn_speak ->{
                speak(et_msg.text.toString())
            }
        }
    }

    override fun bindLayout() = com.demo.barry.demoPage.R.layout.activity_tts

    private fun initTTS(){

        // 设置在线云端
        mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD)
        mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaoqi")
        // 设置发音语速
        mTts.setParameter(SpeechConstant.SPEED, "50")
        // 设置音调
        mTts.setParameter(SpeechConstant.PITCH, "50")
        // 设置合成音量
        mTts.setParameter(SpeechConstant.VOLUME, "100")
        // 设置播放器音频流类型
        mTts.setParameter(SpeechConstant.STREAM_TYPE, "3")
        // 设置播放合成音频打断音乐播放，默认为true
        mTts.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, "true")
        isInitTTS = true
    }

    fun speak(msg:String){
        if (isInitTTS){
            if (mTts.isSpeaking){
                mTts.stopSpeaking()
            }
            mTts.startSpeaking(msg,this)
        }else{
            initTTS()
        }
    }
    override fun onInit(code: Int) {
        if (code == ErrorCode.SUCCESS) {
            isInitTTS = true
        }
    }

    override fun onStop() {
        super.onStop()
        mTts.stopSpeaking()
        mTts.destroy()
    }

    override fun onBufferProgress(p0: Int, p1: Int, p2: Int, p3: String?) {
    }

    override fun onSpeakBegin() {
    }

    override fun onSpeakProgress(p0: Int, p1: Int, p2: Int) {
    }

    override fun onEvent(p0: Int, p1: Int, p2: Int, p3: Bundle?) {
    }

    override fun onSpeakPaused() {
    }

    override fun onSpeakResumed() {
    }

    override fun onCompleted(p0: SpeechError?) {
    }
}