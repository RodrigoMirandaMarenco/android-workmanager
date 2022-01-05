package com.example.background.workers

import WorkManagerTestRule
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo.State.FAILED
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

}