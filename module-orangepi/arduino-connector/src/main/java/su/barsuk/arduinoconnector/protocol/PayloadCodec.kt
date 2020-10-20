package su.barsuk.arduinoconnector.protocol

import su.barsuk.common.extensions.putShort
import su.barsuk.common.extensions.readShort
import kotlin.math.*

private fun Int.constrain(minimum: Int, maximum: Int): Int {
    return min(max(this, minimum), maximum)
}

object PayloadCodec {
    fun getVoltageFromPayload(payload: ByteArray): Int {
        return payload.readShort(0).toInt()
    }

    fun makeSetMotorsPayload(left: Int, right: Int): ByteArray {
        return ByteArray(4).apply {
            putShort(0, left.constrain(-255, 255).toShort())
            putShort(2, right.constrain(-255, 255).toShort())
        }
    }

    fun makeSetLightsPayloadMultiple(vararg colors: Color): ByteArray {
        val data = ByteArray(3 * colors.size)
        for (i in colors.indices) {
            data[3 * i + 0] = colors[i].red.constrain(0, 255).toByte()
            data[3 * i + 1] = colors[i].green.constrain(0, 255).toByte()
            data[3 * i + 2] = colors[i].blue.constrain(0, 255).toByte()
        }
        return data
    }

    fun makeSetLightsPayload(color: Color) = makeSetLightsPayloadMultiple(color)


}