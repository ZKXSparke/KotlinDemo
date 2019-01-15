package com.demo.barry.base_utils.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.provider.MediaStore
import android.widget.EditText
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.demo.barry.base_utils.Common
import com.demo.barry.base_utils.Common.context
import com.orhanobut.logger.Logger
import org.greenrobot.eventbus.EventBus
import org.json.JSONObject
import pers.victor.ext.addTextChangedListener
import pers.victor.ext.app
import pers.victor.ext.toast
import pers.victor.smartgo.SmartPath
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.util.regex.Pattern

/**
 * Created by Victor on 2017/6/29. (ง •̀_•́)ง
 */
@SuppressLint("CheckResult")
//fun ImageView.load(url: Any, isCircleCrop: Boolean = false, holder: Int = R.drawable.ic_head_portrait_s, error: Int = R.drawable.ic_head_portrait_s) {
//    val options = RequestOptions.placeholderOf(holder)
//            .error(error)
//            .diskCacheStrategy(DiskCacheStrategy.ALL)
//            .centerCrop()
//    if (isCircleCrop) {
//        options.circleCrop()
//    }
//
//    if (url is Int) {
//        this.setImageResource(url)
//    } else {
//        Glide.with(this.context)
//                .load(url)
//                .apply(options)
//                .transition(DrawableTransitionOptions.withCrossFade())
//                .into(this)
//    }
//}

//fun ImageView.loadBitmap(url: String, options: RequestOptions) {
//    Glide.with(app)
//            .asBitmap()
//            .load(url)
//            .apply(options)
//            .into(object : SimpleTarget<Bitmap>() {
//                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
//                    setImageBitmap(resource)
//                }
//            })
//}


fun postEvent(event: Any) = EventBus.getDefault().post(event)
fun postStickyEvent(event: Any) = EventBus.getDefault().postSticky(event)

fun log(log: Any?) = Logger.i(log.toString())
fun log(tag: String, log: Any?) = Logger.t(tag).i(log.toString())
fun json(json: Any?) = Logger.json(json.toString())
fun err(error: Any?) = Logger.e(error.toString())


fun showToast(msg: Any) {
    if (msg.toString().length > 10) {
        toast(msg, false)
    } else {
        toast(msg, true)
    }
}


fun Activity.goActivity(path: String) {
    SmartPath.from(this).toPath(path).go()
}

//fun isRtl(): Boolean {
//    return TextUtilsCompat.getLayoutDirectionFromLocale(MultiLanguageUtil.getConfigLocale()) == LayoutDirection.RTL
//}

fun isNetworkAvailable(): Boolean {
    try {
        val connectivity = context
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val info = connectivity.activeNetworkInfo
        if (info != null && info.isConnected) {
            if (info.state == NetworkInfo.State.CONNECTED) {
                return true
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
        return false
    }
    return false
}

//适配Android7.0图片裁剪
fun getImageContentUri(context: Context, imageFile: File): Uri? {
    val filePath = imageFile.absolutePath
    val cursor = context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            arrayOf(MediaStore.Images.Media._ID),
            MediaStore.Images.Media.DATA + "=? ",
            arrayOf(filePath), null)

    return if (cursor != null && cursor.moveToFirst()) {
        val id = cursor.getInt(cursor
                .getColumnIndex(MediaStore.MediaColumns._ID))
        val baseUri = Uri.parse("content://media/external/images/media")
        cursor.close()
        Uri.withAppendedPath(baseUri, "" + id)
    } else {
        if (imageFile.exists()) {
            val values = ContentValues()
            values.put(MediaStore.Images.Media.DATA, filePath)
            context.contentResolver.insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        } else {
            null
        }
    }
}

//val String.formatTemp
//    get() = when (temperatureMode) {
//        0 -> (((this.toIntOrNull() ?: 32) - 32) * 5 / 9).toString() + "°C"
//        else -> "$this°F"
//    }

val String.formatHum
    get() = "$this%"

//fun convertTempToInt(value: String): Int {
//    return when (temperatureMode) {
//        0 -> ((value.toIntOrNull() ?: 32) - 32) * 5 / 9
//        else -> value.toInt()
//    }
//}

fun versionCode(): Int {
    val manager = Common.context.packageManager
    var code = 0
    try {
        val info = manager.getPackageInfo(Common.context.packageName, 0)
        code = info.versionCode
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
    }
    return code
}

fun versionName(): String {
    val manager = Common.context.packageManager
    var code = "1.0"
    try {
        val info = manager.getPackageInfo(Common.context.packageName, 0)
        code = info.versionName
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
    }
    return code
}

/**
 * 识别当前语言
 */
//private fun getSysLanguageStatus(): String {
//    return if (LanguageListManager.languageTypeList.contains(MultiLanguageUtil.getConfigLocale().language)) {
//        MultiLanguageUtil.getConfigLocale().language.toString()
//    } else en
//}
//
//fun getH5Language(): String {
//    return if (languageType.isEmpty()) {
//        val sysLanguage = getSysLanguageStatus().toLowerCase()
//        when {
//            sysLanguage.contains("zh_tw") -> "zh_tw_hk"
//            sysLanguage == "zh_cn" -> "zh_cn"
//            else -> sysLanguage.split("_")[0]
//        }
//    } else {
//        when {
//            languageType.toLowerCase() == "zh" -> "zh_cn"
//            else -> languageType.toLowerCase()
//        }
//    }
//}

fun EditText.setMaxLength(length: Int, isPwd: Boolean = false, callback: () -> Unit) {
    addTextChangedListener {
        after {
            val index = selectionStart - 1
            if (index >= 0) {
                if (length() > length) {
                    callback.invoke()
                    text.delete(index, index + 1)
                } else if (isPwd) {
                    if (text[index] == ' ') {
                        text.delete(index, index + 1)
                    }
                    if (Pattern.compile("[\u4e00-\u9fa5]").matcher(text).find()) {
                        text.delete(index, index + 1)
                    }
                }
            }
        }
    }
}


/**
 *  通过属性名来获取配置文件
 */

fun getJsonProperty(property: String): JSONObject {

    val assetManager = app.assets

    var bf: BufferedReader? = null
    try {
        bf = BufferedReader(InputStreamReader(assetManager.open("config/juhaolian/control.json")))
    } catch (ex: Exception) {
    }
    val root = bf?.readText()
    return JSONObject(root).getJSONObject(property)

}

/**
 * 通过值获取名称
 */
//fun getPropertyName(value: String, property: String): String {
//    val prop = getJsonProperty(property)
//    var modeName = ""
//    when {
//        prop.has("options") -> {
//            val options = prop.getJSONArray("options")
//            (0 until options.length()).forEach {
//                val option = options.getJSONObject(it)
//                if (option.getInt("value").toString() == value) {
//                    modeName = option.getString("des")
//                }
//            }
//        }
//    }
//    return modeName
//}