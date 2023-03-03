package android.ncdev.common.utils.extensions

import android.ncdev.common.utils.tryOptional
import android.util.Patterns
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.net.URL
import java.util.regex.Pattern

fun String.toURL(): URL? {
    tryOptional {
        var url = trim().lowercase()
        if (Patterns.WEB_URL.matcher(url).matches()) {
            if (!url.startsWith("http")) url = "https://$url"
            return URL(url)
        }
    }
    return null
}

fun String.toThrowable(): Throwable {
    return Throwable(this)
}

fun String.isNumeric(): Boolean {
    val pattern = Pattern.compile("-?\\d+(\\.\\d+)?")
    return pattern.matcher(this).matches()
}

fun String.capitalize(): String {
    return if (isNotEmpty()) substring(0, 1).uppercase() + substring(1).lowercase() else this
}

inline fun <reified T> String.jsonToList(): List<T> {
    val typeToken = TypeToken.getParameterized(List::class.java, T::class.java)

    return Gson().fromJson<List<T>>(this, typeToken.type)
}