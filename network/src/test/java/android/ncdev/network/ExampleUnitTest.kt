package android.ncdev.network

import android.ncdev.network.core.simpleGet
import kotlinx.coroutines.runBlocking
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun simpleCall() {
       val test =  runBlocking {
            simpleGet("https://raw.githubusercontent.com/nova-wallet/nova-utils/master/chains/types/default.json")
        }
        println(test)
    }
}