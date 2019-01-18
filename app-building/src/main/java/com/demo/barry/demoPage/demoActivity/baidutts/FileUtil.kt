package com.demo.barry.demoPage.demoActivity.baidutts

import android.content.Context
import android.content.res.AssetManager
import android.os.Environment
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

/**
 * Created by Barry.Zhou on 2019/1/18
 */

class FileUtil(){
    // 创建一个临时目录，用于复制临时文件，如assets目录下的离线资源文件
    fun createTmpDir(context: Context): String {
        val sampleDir = "baiduTTS"
        var tmpDir = Environment.getExternalStorageDirectory().toString() + "/" + sampleDir
        if (!makeDir(tmpDir)) {
            tmpDir = context.getExternalFilesDir(sampleDir)!!.absolutePath
            if (!makeDir(sampleDir)) {
                throw RuntimeException("create model resources dir failed :$tmpDir")
            }
        }
        return tmpDir
    }

    fun fileCanRead(filename: String): Boolean {
        val f = File(filename)
        return f.canRead()
    }

    private fun makeDir(dirPath: String): Boolean {
        val file = File(dirPath)
        return if (!file.exists()) {
            file.mkdirs()
        } else {
            true
        }
    }

    @Throws(IOException::class)
    fun copyFromAssets(assets: AssetManager, source: String, dest: String, isCover: Boolean) {
        val file = File(dest)
        if (isCover || !isCover && !file.exists()) {
            var inputs: InputStream? = null
            var fos: FileOutputStream? = null
            try {
                inputs = assets.open(source)
                fos = FileOutputStream(dest)
                val buffer = ByteArray(1024)
                val size = inputs.read(buffer,0,1024)
                while (size >= 0) {
                    fos.write(buffer, 0, size)
                }
            } finally {
                if (fos != null) {
                    try {
                        fos.close()
                    } finally {
                        inputs?.close()
                    }
                }
            }
        }
    }
}