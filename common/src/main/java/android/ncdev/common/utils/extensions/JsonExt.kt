package android.ncdev.common.utils.extensions

import android.ncdev.common.utils.tryOptional
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.math.BigDecimal
import java.math.BigInteger

fun Any?.asGsonParsedNumberOrNull(): BigInteger? = when (this) {
    // gson parses integers as double when type is not specified
    is Double -> toLong().toBigInteger()
    is Long -> toBigInteger()
    is Int -> toBigInteger()
    is String -> toBigIntegerOrNull()
    else -> null
}
fun Any?.asGsonParsedNumber(): BigInteger = asGsonParsedNumberOrNull()
    ?: throw IllegalArgumentException("Failed to convert gson-parsed object to number")

fun Gson.parseArbitraryObject(src: String): Map<String, Any?>? {
    val typeToken = object : TypeToken<Map<String, Any?>>() {}

    return fromJson(src, typeToken.type)
}

inline fun <reified T> Gson.fromParsedHierarchy(src: Any?): T = fromJson(toJsonTree(src), T::class.java)

inline fun <reified T> Gson.fromJson(src: String): T = fromJson(src, object : TypeToken<T>() {}.type)

inline fun <reified T> Gson.fromJsonOrNull(src: String): T? = runCatching<T> { fromJson(src) }.getOrNull()
inline fun <reified T> T.toJsonString(): String {
    return Gson().toJson(this, T::class.java)
}

inline fun <reified T> String.parseToObject(): T {
    return Gson().fromJson(this, T::class.java)
}

fun JsonObject.optJsonObject(key:String) : JsonObject? {
    return try { get(key).asJsonObject } catch (e:Exception) { null }
}

fun JsonObject.optJsonArray(key:String) : JsonArray? {
    return try { get(key).asJsonArray } catch (e:Exception) { null }
}

fun JsonObject.optInt(key:String, fallback:Int = 0) : Int {
    return try { get(key).asInt } catch (e:Exception) { fallback }
}

fun JsonObject.optLong(key:String, fallback:Long = 0) : Long {
    return try { get(key).asLong } catch (e:Exception) { fallback }
}

fun JsonObject.optFloat(key:String, fallback:Float = 0f) : Float {
    return try { get(key).asFloat } catch (e:Exception) { fallback }
}

fun JsonObject.optDouble(key:String, fallback:Double = 0.0) : Double {
    return try { get(key).asDouble } catch (e:Exception) { fallback }
}

fun JsonObject.optString(key:String, fallback:String = "") : String {
    return get(key)?.takeIf { !it.isJsonNull }?.run { tryOptional { asString } ?: toString()  } ?: fallback
}

fun JsonObject.optBoolean(key:String, fallback:Boolean = false) : Boolean{
    return try { get(key).asBoolean }catch (e : Exception){ fallback }
}

fun JsonObject.optBigDecimal(key: String, fallback: BigDecimal = BigDecimal.ZERO): BigDecimal {
    return try { get(key).asBigDecimal } catch (e: Exception) { fallback }
}

fun isJSONValid(test: String): Boolean {
    try {
        JSONObject(test)
    } catch (ex: JSONException) {
        try {
            JSONArray(test)
        } catch (ex1: JSONException) {
            return false
        }
    }
    return true
}