package su.barsuk.common.extensions

fun ByteArray.toHexString() = joinToString(" ") { "%02X".format(it) }

fun ByteArray.readShort(index: Int): Short {
    return (
            (this[index + 0].toInt() shl 0)
        or (this[index + 1].toInt() shl 8)
    ).toShort()
}

fun ByteArray.putShort(index: Int, value: Short) {
    val intVal = value.toInt()
    this[index + 0] = (intVal shr 0).toByte()
    this[index + 1] = (intVal shr 8).toByte()
}
