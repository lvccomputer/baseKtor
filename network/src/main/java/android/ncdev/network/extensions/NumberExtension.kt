package android.ncdev.network.extensions

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.ionspin.kotlin.bignum.decimal.toBigDecimal
import com.ionspin.kotlin.bignum.integer.BigInteger
import com.ionspin.kotlin.bignum.integer.toBigInteger

fun String.toBigIntegerOrDefault(default:BigInteger) = tryOptional { toBigInteger() } ?: default
fun String.toBigIntegerOrDefault() = toBigIntegerOrDefault(BigInteger.ZERO)

fun String.toBigDecimalOrDefault(default:BigDecimal) = tryOptional { toBigDecimal() } ?: default
fun String.toBigDecimalOrDefault() = toBigDecimalOrDefault(BigDecimal.ZERO)