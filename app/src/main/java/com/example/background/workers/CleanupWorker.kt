package com.example.background.workers

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.background.OUTPUT_PATH
import com.example.background.R
import java.io.File

/**
 * Created by rodrigomiranda on 4/1/22.
 */
class CleanupWorker(context: Context, params: WorkerParameters) : Worker(context, params) {

    override fun doWork(): Result {
        val appContext = applicationContext
        makeStatusNotification(appContext.getString(R.string.cleaning_up), appContext)
        sleep()

        return try {
            val outputDirectory = File(appContext.filesDir, OUTPUT_PATH)
            if (outputDirectory.exists()) {
                val entries = outputDirectory.listFiles()
                if (entries != null) {
                    for (entry in entries) {
                        val name = entry.name
                        if (name.isNotEmpty() && name.endsWith(".png")) {
                            val deleted = entry.delete()
                            Log.i(TAG, "Deleted $name - $deleted")
                        }
                    }
                }
            }
            Result.success()
        } catch (ex: Exception) {
            ex.printStackTrace()
            Result.failure()
        }
    }

    companion object {
        const val TAG = "CleanupWorker"
    }
}