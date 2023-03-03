package android.ncdev.network.extensions

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.ionspin.kotlin.bignum.integer.BigInteger
import com.ionspin.kotlin.bignum.serialization.kotlinx.bigdecimal.bigDecimalHumanReadableSerializerModule
import com.ionspin.kotlin.bignum.serialization.kotlinx.biginteger.bigIntegerhumanReadableSerializerModule
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.*
import kotlinx.serialization.modules.SerializersModule

inline fun <T> tryOptional(expression: () -> T): T? {
    return try {
        expression()
    } catch (ex: Throwable) {
        null
    }
}

val jsonNonstrict get() = Json {
    isLenient = true
    ignoreUnknownKeys = true
    encodeDefaults = true
    serializersModule = SerializersModule {
        include(bigIntegerhumanReadableSerializerModule)
        include(bigDecimalHumanReadableSerializerModule)
    }
}
inline fun <reified T> T.toJsonString(): String {
    return jsonNonstrict.encodeToString(this)
}

inline fun <reified T> JsonElement.parseToObject(): T {
    return jsonNonstrict.decodeFromJsonElement(this)
}

inline fun <reified T> String.parseToObject(): T {
    return jsonNonstrict.decodeFromString(this)
}

fun String.toJsonElement(): JsonElement {
    return try {
        jsonNonstrict.parseToJsonElement(this)
    } catch (e: Exception) {
        throw Exception(this)
    }
}

fun String.toJsonObject(): JsonObject = toJsonElement().jsonObject

fun String.toJsonArray(): JsonArray = toJsonElement().jsonArray

private fun convertToJsonElement(value:Any): JsonElement {
    return when(value) {
        is JsonElement -> value
        is String -> JsonPrimitive(value)
        is Number -> JsonPrimitive(value)
        is Boolean -> JsonPrimitive(value)
        is List<*> -> {
            val jsonElements = value.mapNotNull { it?.run{convertToJsonElement(this)} }
            Json.encodeToJsonElement(ListSerializer(JsonElement.serializer()), jsonElements)
        }
        is Map<*, *> -> Json.encodeToJsonElement(MapSerializer(
            String.serializer(),
            JsonElement.serializer()
        ), value
            .mapKeys { it.key.toString() }
            .mapValues { it.value!!.run { convertToJsonElement(this) } })
        else -> JsonPrimitive(value.toString())
    }
}

fun Map<String, Any>.toJsonObject() =
    run { Json.encodeToJsonElement(mapValues { convertToJsonElement(it.value) }).jsonObject }

fun JsonObject.optString(key: String, defaultValue: String = ""): String {
    val content = runCatching { this[key]?.jsonPrimitive?.contentOrNull }.getOrElse { this[key].toString() }
    if (content.isNullOrBlank()) return defaultValue
    return content
}

fun JsonObject.optString(keys: Array<String>): String {
    for (key in keys) {
        val value = this[key]?.jsonPrimitive
        if (value?.contentOrNull != null && value.content.isNotBlank()) {
            return value.content
        }
    }
    return ""
}

fun JsonObject.optStringInPath(vararg path: String): String {
    var jsonValue: JsonObject? = this
    path.forEach {
        val j = jsonValue ?: return@forEach
        jsonValue = j.getJsonObjectOrNull(it) ?: return j.optString(it)
    }
    return ""
}

fun JsonObject.optDouble(key: String, defaultValue: Double = 0.0): Double {
    return this[key]?.jsonPrimitive?.doubleOrNull ?: return optString(key).toDoubleOrNull()
        ?: defaultValue
}

fun JsonObject.optInt(key: String, defaultValue: Int = 0): Int {
    return this[key]?.jsonPrimitive?.intOrNull ?: return optString(key).toIntOrNull()
        ?: defaultValue
}

fun JsonObject.optLong(key: String, defaultValue: Long = 0): Long {
    return this[key]?.jsonPrimitive?.longOrNull ?: return optString(key).toLongOrNull()
        ?: defaultValue
}

fun JsonObject.optBool(key: String, defaultValue: Boolean = false): Boolean {
    return this[key]?.jsonPrimitive?.booleanOrNull ?: return optString(
        key,
        defaultValue.toString()
    ).toBoolean()
}

fun JsonObject.optBigInteger(key: String, fallback: BigInteger = BigInteger.ZERO): BigInteger {
    return optString(key).toBigIntegerOrDefault(fallback)
}

fun JsonObject.optBigDecimal(key: String, fallback: BigDecimal = BigDecimal.ZERO): BigDecimal {
    return optString(key).toBigDecimalOrDefault(fallback)
}

fun JsonObject.optBigDecimalInPath(
    vararg path: String,
    fallback: BigDecimal = BigDecimal.ZERO
): BigDecimal {
    var jsonValue: JsonObject? = this
    path.forEach {
        val j = jsonValue ?: return@forEach
        jsonValue = j.getJsonObjectOrNull(it) ?: return j.optBigDecimal(it, fallback)
    }
    return fallback
}

@Throws(NoSuchElementException::class)
fun JsonObject.getJsonObject(key: String): JsonObject {
    return tryOptional { getValue(key).jsonObject } ?: optString(key).toJsonObject()
}

@Throws(NoSuchElementException::class)
fun JsonObject.getJsonArray(key: String): JsonArray {
    return tryOptional { getValue(key).jsonArray } ?: optString(key).toJsonArray()
}

fun JsonObject.getJsonObjectOrNull(key: String): JsonObject? {
    return tryOptional { getJsonObject(key) }
}

fun JsonObject.getJsonArrayOrNull(key: String): JsonArray? {
    return tryOptional { getJsonArray(key) }
}

fun String.unEscapeJson():String{
   return this.replace("\\","")
        .replace("}\"","}")
        .replace("\"{","{")
}