package su.barsuk.connectivity.protocol

import su.barsuk.common.extensions.toHexString

internal data class Message(
        val messageType: MessageType,
        val data: ByteArray = byteArrayOf()
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Message

        if (messageType != other.messageType) return false
        if (!data.contentEquals(other.data)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = messageType.hashCode()
        result = 31 * result + data.contentHashCode()
        return result
    }

    override fun toString(): String {
        return "${Message::class.simpleName}(messageType=$messageType, data=[${data.toHexString()}])"
    }
}