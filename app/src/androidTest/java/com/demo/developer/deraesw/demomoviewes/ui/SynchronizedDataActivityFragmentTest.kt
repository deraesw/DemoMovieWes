package com.demo.developer.deraesw.demomoviewes.ui

import androidx.navigation.findNavController
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.demo.developer.deraesw.demomoviewes.R
import com.demo.developer.deraesw.demomoviewes.di.SyncRepositoryModule
import com.demo.developer.deraesw.demomoviewes.extension.*
import com.demo.developer.deraesw.demomoviewes.repository.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class TestModule {

    @Singleton
    @Provides
    fun provideSyncRepository(
        genreRepository: MovieGenreRepository,
        sharePrefRepository: SharePrefRepository,
        movieCreditsRepository: MovieCreditsRepository,
        movieRepository: MovieRepository
    ): SyncRepositoryInterface = SyncRepositoryTest()
}


@RunWith(AndroidJUnit4::class)
@UninstallModules(SyncRepositoryModule::class)
@HiltAndroidTest
class SynchronizedDataActivityFragmentTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val activityTestRule = ActivityScenarioRule(MainActivity::class.java)

    @Inject
    lateinit var repository: SyncRepositoryInterface

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Before
    fun jumpToSyncFragment() {
        activityTestRule.scenario.onActivity {
            it.runOnUiThread {
                it.findNavController(R.id.main_container)
                    .navigate(R.id.synchronizedDataActivityFragment)
            }
        }
    }

    @Test
    fun testSyncFragment_MainUI() {
        runBlocking {
            (repository as SyncRepositoryTest).setNoSync()
            delay(2000)
        }

        R.id.cardView2.withId().isDisplay()

        R.id.tv_title_app.withId().isText(R.string.app_name)

        R.string.lbl_loading_please_wait.withText().isDisplay()

        R.id.iv_sync_failed.withId().isNotDisplay()

        R.id.iv_sync_success.withId().isNotDisplay()

        R.id.progress_bar.withId().isNotDisplay()

        R.id.btn_retry.withId().isNotDisplay()
    }

    @Test
    fun testSyncFragment_SyncInProgressState() {

        runBlocking {
            (repository as SyncRepositoryTest).setSyncInProgress()
            delay(500)
        }

        R.id.cardView2.withId().isDisplay()

        R.id.tv_title_app.withId().isText(R.string.app_name)

        R.string.lbl_loading_please_wait.withText().isDisplay()

        R.id.iv_sync_failed.withId().isNotDisplay()

        R.id.iv_sync_success.withId().isNotDisplay()

        R.id.progress_bar.withId().isDisplay()

        R.id.btn_retry.withId().isNotDisplay()
    }

    @Test
    fun testSyncFragment_SyncDoneState() {

        runBlocking {
            (repository as SyncRepositoryTest).setSyncDone()
            delay(500)
        }

        R.id.cardView2.withId().isDisplay()

        R.id.tv_title_app.withId().isText(R.string.app_name)

        R.string.lbl_loading_please_wait.withText().isDisplay()

        R.id.iv_sync_failed.withId().isNotDisplay()

        R.id.iv_sync_success.withId().isDisplay()

        R.id.progress_bar.withId().isNotDisplay()

        R.id.btn_retry.withId().isNotDisplay()
    }

    @Test
    fun testSyncFragment_FailedState() {

        runBlocking {
            (repository as SyncRepositoryTest).setSyncFailed()
            delay(1000)
        }

        R.id.cardView2.withId().isDisplay()

        R.id.tv_title_app.withId().isText(R.string.app_name)

        R.string.lbl_loading_please_wait.withText().isDisplay()

        R.id.iv_sync_failed.withId().isDisplay()

        R.id.iv_sync_success.withId().isNotDisplay()

        R.id.progress_bar.withId().isNotDisplay()

        R.id.btn_retry.withId().isDisplay().isText(R.string.lbl_retry)
    }
}