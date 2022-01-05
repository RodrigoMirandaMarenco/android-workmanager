package com.example.background.workers

import WorkManagerTestRule
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo.State.FAILED
import androidx.work.WorkInfo.State.SUCCEEDED
import androidx.work.workDataOf
import com.example.background.KEY_IMAGE_URI
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.assertThat
import org.junit.Rule
import org.junit.Test

/**
 * Created by rodrigomiranda on 5/1/22.
 */
class BlurWorkerTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var workManagerRule = WorkManagerTestRule()

    @Test
    fun testFailsIfNoInput() {
        val request = OneTimeWorkRequestBuilder<BlurWorker>().build()
        workManagerRule.workManager.enqueue(request).result.get()
        val info = workManagerRule.workManager.getWorkInfoById(request.id).get()
        assertThat(info.state, `is`(FAILED))
    }

    @Test
    @Throws(Exception::class)
    fun testAppliesBlur() {
        val inputDataUri = copyFileFromTestToTargetCtx(
            workManagerRule.testContext,
            workManagerRule.targetContext,
            "test_image.png"
        )
        val inputData = workDataOf(KEY_IMAGE_URI to inputDataUri.toString())

        val request = OneTimeWorkRequestBuilder<BlurWorker>().setInputData(inputData).build()
        workManagerRule.workManager.enqueue(request).result.get()
        val info = workManagerRule.workManager.getWorkInfoById(request.id).get()
        val outputUri = info.outputData.getString(KEY_IMAGE_URI)

        assertThat(uriFileExists(workManagerRule.targetContext, outputUri), `is`(true))
        assertThat(info.state, `is`(SUCCEEDED))
    }
}