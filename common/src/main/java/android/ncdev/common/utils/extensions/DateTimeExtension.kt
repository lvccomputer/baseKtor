package android.ncdev.common.utils.extensions

import android.content.Context
import android.text.format.DateUtils
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit

//fun Long.toDate() = run {
//    var time = this
//    if (time < 10e10) {
//        // if timestamp given in seconds, convert to millis
//        time *= 1000
//    }
//    Date(time)
//}

const val DATE = "MMMM dd, yyyy"
const val DATE_NEW = "MMM dd/yyyy"
const val DATE_TIME = "HH:mm - MMMM dd, yyyy"
const val DATE_MONTH = "MMM dd"

val CURRENT_TIME get() = ZonedDateTime.now().toEpochSecond()

fun Long.toZonedDateTime(): ZonedDateTime {
    var time = this
    if (time < 10e10) {
        // if timestamp given in seconds, convert to millis
        time *= 1000
    }
    val zone = ZoneId.systemDefault()
    return Instant.ofEpochMilli(time).atZone(zone)
}

fun Long.toTimeMillisecond():Long{
    var time = this
    if (time < 10e10) {
        // if timestamp given in seconds, convert to millis
        time *= 1000
    }
    return time
}

fun ZonedDateTime.formatWithPattern(pattern: String): String {
    return format(DateTimeFormatter.ofPattern(pattern))
}

fun getPreviousDayZonedDateTime(differenceAsDay: Long): ZonedDateTime {
    return ZonedDateTime.now().minusDays(differenceAsDay)
}

fun getCurrentTimeAsZonedDateTime(): ZonedDateTime {
    return ZonedDateTime.now()
}

fun comparePreAndCurrentDate(previousZonedDateTime: ZonedDateTime): Boolean {
    val preDate = previousZonedDateTime.formatWithPattern(DATE)
    val currentDate = getCurrentTimeAsZonedDateTime().formatWithPattern(DATE)
    return preDate != currentDate
}

fun Long.convertTime(): String {
    val newTime = toZonedDateTime()
    val currentTime = CURRENT_TIME
    val subTime = currentTime - newTime.toEpochSecond()
    return when {
        newTime.formatWithPattern(DATE_NEW) == getPreviousDayZonedDateTime(1).formatWithPattern(
            DATE_NEW
        ) -> {
            "Yesterday"
        }
        subTime < 60 -> {
            subTime.toString() + "s"
        }
        subTime < 3_600 -> {
            (subTime / 60).toString() + "m"
        }
        subTime < 86_400 -> {
            (subTime / 3_600).toString() + "h"
        }
        else -> {
            newTime.formatWithPattern(DATE_NEW)
        }
    }
}
fun Long.formatToDate():String{
    val newTime = toZonedDateTime()
    if (isToday()){
        return "Today"
    }
    if (isYesterday())
        return "Yesterday"
    return newTime.formatWithPattern(DATE_NEW)
}
fun Long.isToday():Boolean{
    return toZonedDateTime().formatWithPattern(DATE) == getCurrentTimeAsZonedDateTime().formatWithPattern(DATE)
}
fun Long.isYesterday():Boolean{
    return toZonedDateTime().formatWithPattern(DATE) == getPreviousDayZonedDateTime(1).formatWithPattern(DATE)
}

fun Long.daysFromMillis() = TimeUnit.MILLISECONDS.toDays(this)

fun Long.formatDaysSinceEpoch(context: Context): String? {
    val currentDays = System.currentTimeMillis().daysFromMillis()
    val diff = currentDays - this

    if (diff < 0) throw IllegalArgumentException("Past date should be less than current")

    return when (diff) {
        0L -> "Today"
        1L -> "Yesterday"
        else -> {
            val inMillis = TimeUnit.DAYS.toMillis(this)
            DateUtils.formatDateTime(context, inMillis, 0)
        }
    }
}