package com.example.background.workers

import WorkManagerTestRule
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo.State.SUCCEEDED
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.assertThat
import org.junit.Rule
import org.junit.Test

/**
 * Created by rodrigomiranda on 5/1/22.
 */
class CleanupWorkerTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var workManagerRule = WorkManagerTestRule()

    @Test
    fun testCleanupWork() {
        val testUri = copyFileFromTestToTargetCtx(
            workManagerRule.testContext,
            workManagerRule.targetContext,
            "test_image.png"
        )
        assertThat(uriFileExists(workManagerRule.targetContext, testUri.toString()), `is`(true))

        val request = OneTimeWorkRequestBuilder<CleanupWorker>().build()
        workManagerRule.workManager.enqueue(request).result.get()
        val workInfo = workManagerRule.workManager.getWorkInfoById(request.id).get()

        assertThat(uriFileExists(workManagerRule.targetContext, testUri.toString()), `is`(false))
        assertThat(workInfo.state, `is`(SUCCEEDED))
    }
}