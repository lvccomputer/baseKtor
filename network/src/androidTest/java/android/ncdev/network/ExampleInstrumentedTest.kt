package android.ncdev.network

import android.ncdev.network.core.simpleGet
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("android.ncdev.network.test", appContext.packageName)
    }

    @Test
    suspend fun simpleCall() {
        runCatching {
            simpleGet("https://raw.githubusercontent.com/nova-wallet/nova-utils/master/chains/types/default.json")
        }.onFailure {
            println(it.message)
        }.onSuccess {
            println(it)
        }
    }
}