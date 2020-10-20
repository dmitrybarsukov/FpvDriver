package su.barsuk.arduinoconnector.protocol

object CommandCodec {
    fun encode(command: Command): ByteArray {
        return byteArrayOf(command.commandType.byteValue, *command.data)
    }

    fun decode(byteArray: ByteArray): Command? {
        if(byteArray.isEmpty())
            return null
        val type = CommandType.getByByteValueOrNull(byteArray[0]) ?: return null
        val data = byteArray.drop(1).toByteArray()
        return Command(type, data)
    }
}