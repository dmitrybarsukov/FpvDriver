package su.barsuk.arduinoconnector.protocol

enum class CommandType(value: Int) {
    NONE(0x00),
    OK(0xA0),
    GET_VOLTAGE(0xC0),
    SET_MOTORS(0xC1),
    SET_LIGHT(0xC2),
    ERROR_TOO_MANY_BYTES(0xE0),
    ERROR_COMMAND_INVALID(0xE1),
    ERROR_PARAMS_INVALID(0xE2);

    val byteValue = value.toByte()

    companion object {
        fun getByByteValueOrNull(value: Byte?): CommandType? {
            return values().firstOrNull { it.byteValue == value }
        }
    }
}