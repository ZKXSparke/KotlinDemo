package com.demo.barry.demoPage.demoActivity.baidutts

import com.baidu.tts.client.SpeechSynthesizerListener
import com.baidu.tts.client.TtsMode

/**
 * Created by Barry.Zhou on 2019/1/18
 */

class InitConfig(
        /**
         * appId appKey 和 secretKey。注意如果需要离线合成功能,请在您申请的应用中填写包名。
         * 本demo的包名是com.baidu.tts.sample，定义在build.gradle中。
         */
        var appId: String, var appKey: String, var secretKey: String, ttsMode: TtsMode, params: Map<String, String>, listener: SpeechSynthesizerListener) {

    /**
     * 纯在线或者离在线融合
     */
    var ttsMode: TtsMode? = ttsMode


    /**
     * 初始化的其它参数，用于setParam
     */
    var params: Map<String, String>? = params

    /**
     * 合成引擎的回调
     */
    var listener: SpeechSynthesizerListener? = listener


}

