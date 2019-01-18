package com.demo.barry.demoPage.demoActivity.baidutts

import android.content.Context
import android.content.res.AssetManager
import android.util.Log
import java.io.IOException
import java.util.HashMap

/**
 * Created by Barry.Zhou on 2019/1/18
 */

class OfflineResource() {

    val VOICE_FEMALE = "F"

    val VOICE_MALE = "M"


    val VOICE_DUYY = "Y"

    val VOICE_DUXY = "X"

    private val SAMPLE_DIR = "baiduTTS"

    private lateinit var assets: AssetManager
    private lateinit var destPath: String

    private var textFilename: String? = null
    private var modelFilename: String? = null

    private val mapInitied = HashMap<String, Boolean>()

    @Throws(IOException::class)
    fun OfflineResource(context: Context, voiceType: String) {
        var context = context
        context = context.applicationContext
        this.assets = context.applicationContext.assets
        this.destPath = FileUtil().createTmpDir(context)
        setOfflineVoiceType(voiceType)
    }

    @Throws(IOException::class)
    fun setOfflineVoiceType(voiceType: String) {
        val text = "bd_etts_text.dat"
        val model: String
        if (VOICE_MALE == voiceType) {
            model = "bd_etts_common_speech_m15_mand_eng_high_am-mix_v3.0.0_20170505.dat"
        } else if (VOICE_FEMALE == voiceType) {
            model = "bd_etts_common_speech_f7_mand_eng_high_am-mix_v3.0.0_20170512.dat"
        } else if (VOICE_DUXY == voiceType) {
            model = "bd_etts_common_speech_yyjw_mand_eng_high_am-mix_v3.0.0_20170512.dat"
        } else if (VOICE_DUYY == voiceType) {
            model = "bd_etts_common_speech_as_mand_eng_high_am_v3.0.0_20170516.dat"
        } else {
            throw RuntimeException("voice type is not in list")
        }
        textFilename = copyAssetsFile(text)
        modelFilename = copyAssetsFile(model)

    }


    @Throws(IOException::class)
    private fun copyAssetsFile(sourceFilename: String): String {
        val destFilename = "$destPath/$sourceFilename"
        var recover = false
        val existed = mapInitied[sourceFilename] // 启动时完全覆盖一次
        if (existed == null || !existed) {
            recover = true
        }
        FileUtil().copyFromAssets(assets, sourceFilename, destFilename, recover)
        return destFilename
    }

    companion object {

        val VOICE_FEMALE = "F"

        val VOICE_MALE = "M"


        val VOICE_DUYY = "Y"

        val VOICE_DUXY = "X"

        private val SAMPLE_DIR = "baiduTTS"

        private val mapInitied = HashMap<String, Boolean>()
    }


}
