package su.barsuk.arduinoconnector.protocol

import su.barsuk.common.extensions.toHexString

data class Command(
        val commandType: CommandType,
        val data: ByteArray = byteArrayOf()
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Command

        if (commandType != other.commandType) return false
        if (!data.contentEquals(other.data)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = commandType.hashCode()
        result = 31 * result + data.contentHashCode()
        return result
    }

    override fun toString(): String {
        return "${Command::class.simpleName}(commandType=$commandType, data=[${data.toHexString()}])"
    }
}