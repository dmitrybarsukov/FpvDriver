package su.barsuk.arduinoconnector.protocol

internal object MessageCodec {
    fun encode(message: Message): ByteArray {
        return byteArrayOf(message.messageType.byteValue, *message.data)
    }

    fun decode(byteArray: ByteArray): Message? {
        if(byteArray.isEmpty())
            return null
        val type = MessageType.getByByteValueOrNull(byteArray[0]) ?: return null
        val data = byteArray.drop(1).toByteArray()
        return Message(type, data)
    }
}