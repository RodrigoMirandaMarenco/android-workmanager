package com.example.background.workers

import android.content.Context
import android.net.Uri
import com.example.background.OUTPUT_PATH
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.*

/**
 * Created by rodrigomiranda on 5/1/22.
 */
@Throws(Exception::class)
fun copyFileFromTestToTargetCtx(testCtx: Context, targetCtx: Context, fileName: String): Uri {
    val destinationFileName = String.format("blur-test-%s.png", UUID.randomUUID().toString())
    val outputDir = File(targetCtx.filesDir, OUTPUT_PATH)
    if (!outputDir.exists()) {
        outputDir.mkdir()
    }
    val outputFile = File(outputDir, destinationFileName)

    val bis = BufferedInputStream(testCtx.assets.open(fileName))
    val bos = BufferedOutputStream(FileOutputStream(outputFile))
    val buf = ByteArray(1024)
    bis.read(buf)
    do {
        bos.write(buf)
    } while (bis.read(buf) != -1)
    bis.close()
    bos.close()

    return Uri.fromFile(outputFile)
}

