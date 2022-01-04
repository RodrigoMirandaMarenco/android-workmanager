package com.example.background.workers

import android.content.Context
import android.graphics.BitmapFactory
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.background.R

/**
 * Created by rodrigomiranda on 4/1/22.
 */
class BlurWorker(context: Context, params: WorkerParameters) : Worker(context, params) {

    override fun doWork(): Result {
        val appContext = applicationContext
        makeStatusNotification(appContext.getString(R.string.blurring_image), appContext)

        return try {
            val picture =
                BitmapFactory.decodeResource(appContext.resources, R.drawable.android_cupcake)
            val blurredPicture = blurBitmap(picture, appContext)
            val uri = writeBitmapToFile(appContext, blurredPicture)
            makeStatusNotification(appContext.getString(R.string.output_is, uri), appContext)
            Result.success()
        } catch (e: Throwable) {
            Log.e(TAG, "Error applying blur")
            Result.failure()
        }
    }

    companion object {
        const val TAG = "BlurWorker"
    }
}