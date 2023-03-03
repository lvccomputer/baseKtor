package android.ncdev.common.utils.extensions

import java.math.BigDecimal
import java.math.RoundingMode
import java.text.NumberFormat
import java.util.*

fun BigDecimal.isZero(): Boolean {
    return compareTo(BigDecimal.ZERO) == 0
}

fun BigDecimal.isNotZero(): Boolean {
    return compareTo(BigDecimal.ZERO) != 0
}

fun BigDecimal.formatNumber(prefix:String = "", suffix:String = "", maxFraction:Int? = null, isGroupingUsed:Boolean = true):String {
    val fractions = maxFraction ?: when {
        this > 1e6.toBigDecimal() -> 2
        this > 1e3.toBigDecimal() -> 4
        else -> 6
    }

    val numberFormat = NumberFormat.getNumberInstance(Locale.US)
    numberFormat.isGroupingUsed = isGroupingUsed
    numberFormat.maximumFractionDigits = fractions
    numberFormat.minimumFractionDigits = 0
    numberFormat.roundingMode = RoundingMode.DOWN

    var suf = suffix
    if (suffix.isNotEmpty() && !suffix.startsWith(" ")) {
        suf = " $suffix"
    }
    return "$prefix${numberFormat.format(this)}$suf"
}

fun getValueFromPercentage(percentage: Int, total: Float): Float {
    return (percentage.toFloat() / 100f) * total
}