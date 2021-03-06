package com.mwigzell.places.repository

import android.content.SharedPreferences

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mwigzell.places.TestApplication
import com.mwigzell.places.repository.api.FileService

import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 *
 */
@RunWith(AndroidJUnit4::class)
class FileServiceTest {

    lateinit internal var fileService: FileService
    lateinit internal var preferences: SharedPreferences

    @Before
    fun setup() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext

        val app = context.applicationContext as TestApplication
        app.getInjector().inject(this)


        preferences = context.getSharedPreferences("data", 0)
        preferences.edit().remove("data").commit()

        fileService = FileService(context)
    }

    @After
    fun teardown() {
        preferences.edit().remove("data").commit()
    }

    @Test
    @Throws(Exception::class)
    fun loadTypes() {
        fileService.fetchTypes()
                .subscribe {
                    assertTrue(it.size > 0)
                }
    }
}
