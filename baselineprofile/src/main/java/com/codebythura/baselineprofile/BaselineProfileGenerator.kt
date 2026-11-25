package com.codebythura.baselineprofile

import androidx.benchmark.macro.junit4.BaselineProfileRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.uiAutomator
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class BaselineProfileGenerator {

    @get:Rule
    val rule = BaselineProfileRule()

    private val packageName : String by lazy {
        InstrumentationRegistry.getArguments().getString("targetAppId")
            ?: throw Exception("targetAppId not passed as instrumentation runner arg")
    }

    @Test
    fun generate() {
        rule.collect(
            packageName = packageName,
            includeInStartupProfile = true
        ) {
            uiAutomator {
                pressHome()
                startApp(packageName)
                dismissResumeDialogIfShown()
                swipeGameContent()
            }
        }
    }
}