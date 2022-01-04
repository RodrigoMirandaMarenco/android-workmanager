package com.example.background.workers

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.background.KEY_IMAGE_URI
import com.example.background.R
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by rodrigomiranda on 4/1/22.
 */
class SaveImageToFileWorker(context: Context, params: WorkerParameters) : Worker(context, params) {

    private val title = "Blurred image"
    private val dateFormatter = SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss z", Locale.getDefault())

    override fun doWork(): Result {
        val appContext = applicationContext
        makeStatusNotification(appContext.getString(R.string.saving_image), appContext)
        sleep()
        val resolver = appContext.contentResolver

        return try {
            val resourceUri = inputData.getString(KEY_IMAGE_URI)
            val bitmap =
                BitmapFactory.decodeStream(resolver.openInputStream(Uri.parse(resourceUri)))
            val imageUrl = MediaStore.Images.Media.insertImage(
                resolver,
                bitmap,
                title,
                dateFormatter.format(Date())
            )

            if (!imageUrl.isNullOrEmpty()) {
                Result.success(workDataOf(KEY_IMAGE_URI to imageUrl))
            } else {
                Result.failure()
            }

        } catch (ex: Exception) {
            ex.printStackTrace()
            Result.failure()
        }
    }

    companion object {
        const val TAG = "SaveImageToFileWorker"
    }
}