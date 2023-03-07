package android.ncdev.girl_photo.network.services

import android.ncdev.network.core.BaseUrl
import android.ncdev.network.core.HttpProvider
import android.ncdev.network.extensions.parseToObject
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.http.*

class GirlService : BaseUrl {
    override fun getBaseUrl() =
        "https://raw.githubusercontent.com/lvccomputer/girlJson/master/girl.json"

    private val girlSDK = HttpProvider(this) {

//        defaultRequest {
//            contentType(ContentType.Application.Json)
//            header("", "")
//        }
    }

    @Throws(Throwable::class)
    suspend fun getGirls(): List<String> {
        return girlSDK.simpleGet(getBaseUrl()).parseToObject()
    }
}