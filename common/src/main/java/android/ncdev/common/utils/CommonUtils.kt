package android.ncdev.common.utils

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.delay
import java.io.InputStream
import java.math.BigDecimal

inline fun <T> tryOptional(expression: () -> T): T? {
    return try {
        expression()
    } catch (ex: Throwable) {
        null
    }
}
fun <T> Result<T>.requireValue() = getOrThrow()!!
fun <T> Result<T>.requireException() = exceptionOrNull()!!

suspend inline fun <T> retryUntilDone(
    block: () -> T
): T {
    var attempt = 0

    while (true) {
        val blockResult = runCatching { block() }

        if (blockResult.isSuccess) {
            return blockResult.requireValue()
        } else {
            attempt++

            delay(attempt * 500L)
        }
    }
}
inline fun <T> T?.defaultOnNull(lazyProducer: () -> T): T {
    return this ?: lazyProducer()
}
fun <T> MutableLiveData<T>.asLiveData() : LiveData<T>{
    return this
}
fun <T> Iterable<T>.isAscending(comparator: Comparator<T>) = zipWithNext().all { (first, second) -> comparator.compare(first, second) < 0 }
inline fun <T, R> Iterable<T>.mapToSet(mapper: (T) -> R): Set<R> = mapTo(mutableSetOf(), mapper)
@Suppress("UNCHECKED_CAST")
inline fun <K, V> Map<K, V?>.filterNotNull(): Map<K, V> {
    return filterValues { it != null } as Map<K, V>
}

fun InputStream.readText() = bufferedReader().use { it.readText() }
fun ByteArrayComparator() = Comparator<ByteArray> { a, b -> a.compareTo(b) }

operator fun ByteArray.compareTo(other: ByteArray): Int {
    if (size != other.size) {
        return size - other.size
    }

    for (i in 0 until size) {
        val result = this[i].compareTo(other[i])

        if (result != 0) {
            return result
        }
    }

    return 0
}
inline fun <T> List<T>.sumByBigDecimal(extractor: (T) -> BigDecimal) = fold(BigDecimal.ZERO) { acc, element ->
    acc + extractor(element)
}

fun <T> List<T>.removed(condition: (T) -> Boolean): List<T> {
    return toMutableList().apply { removeAll(condition) }
}
fun DialogFragment.showWithStateCheck(fragmentManager: FragmentManager?, tag: String = "") {
    if (fragmentManager != null && fragmentManager.isStateSaved.not()) {
        show(fragmentManager, tag)
    }
}