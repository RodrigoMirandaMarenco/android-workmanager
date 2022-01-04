package com.example.background.workers

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.text.TextUtils
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.background.KEY_IMAGE_URI
import com.example.background.R

/**
 * Created by rodrigomiranda on 4/1/22.
 */
class BlurWorker(context: Context, params: WorkerParameters) : Worker(context, params) {

    override fun doWork(): Result {
        val appContext = applicationContext
        val resourceUri = inputData.getString(KEY_IMAGE_URI)
        makeStatusNotification(appContext.getString(R.string.blurring_image), appContext)

        return try {
            if (TextUtils.isEmpty(resourceUri)) {
                Log.e(TAG, "Empty resourceUri")
                throw IllegalArgumentException("Empty resourceUri")
            }

            val picture =
                BitmapFactory.decodeStream(
                    appContext.contentResolver.openInputStream(
                        Uri.parse(
                            resourceUri
                        )
                    )
                )
            val blurredPicture = blurBitmap(picture, appContext)
            val outputUri = writeBitmapToFile(appContext, blurredPicture)
            makeStatusNotification(appContext.getString(R.string.output_is, outputUri), appContext)

            Result.success(workDataOf(KEY_IMAGE_URI to outputUri.toString()))
        } catch (e: Throwable) {
            Log.e(TAG, "Error applying blur")
            Result.failure()
        }
    }

    companion object {
        const val TAG = "BlurWorker"
    }
}