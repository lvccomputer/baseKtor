package android.ncdev.network.config

private fun Char.hexToInt() =
    when (this) {
        '0' -> 0
        '1' -> 1
        '2' -> 2
        '3' -> 3
        '4' -> 4
        '5' -> 5
        '6' -> 6
        '7' -> 7
        '8' -> 8
        '9' -> 9
        'a', 'A' -> 10
        'b', 'B' -> 11
        'c', 'C' -> 12
        'd', 'D' -> 13
        'e', 'E' -> 14
        'f', 'F' -> 15
        else -> -1
    }
private fun Char.getNibbleValue() = hexToInt().also {
    if (it == -1) throw IllegalArgumentException("Not a valid hex char: $this")
}

/**
 *  chars for nibble
 */
private const val CHARS = "0123456789abcdef"

/**
 *  Returns 2 char hex string for Byte
 */
fun Byte.toHexString() = toInt().let {
    CHARS[it.shr(4) and 0x0f].toString() + CHARS[it.and(0x0f)].toString()
}

val ByteArray.hex: String get() = joinToString("") { it.toHexString() }
val ByteArray.hexWith0x: String get() = "0x" + joinToString("") { it.toHexString() }


fun String.hexToByteArray(): ByteArray {
    if (length % 2 != 0)
        throw IllegalArgumentException("hex-string must have an even number of digits (nibbles)")

    val cleanInput = if (startsWith("0x")) drop(2) else this

    return ByteArray(cleanInput.length / 2).apply {
        var i = 0
        while (i < cleanInput.length) {
            this[i / 2] = ((cleanInput[i].getNibbleValue() shl 4) + cleanInput[i + 1].getNibbleValue()).toByte()
            i += 2
        }
    }
}