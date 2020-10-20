package su.barsuk.arduinoconnector

import su.barsuk.arduinoconnector.protocol.Color
import su.barsuk.arduinoconnector.protocol.Command
import su.barsuk.arduinoconnector.protocol.CommandType
import su.barsuk.arduinoconnector.protocol.PayloadCodec
import su.barsuk.common.events.Event2
import su.barsuk.common.extensions.putShort
import su.barsuk.common.threading.ThreadBase
import su.barsuk.common.threading.ThreadState

class ArduinoController(
        portName: String
) {
    var connector = ArduinoConnector(portName)

    val eventThreadStateChanged = Event2<ThreadBase, ThreadState>()
    val eventThreadException = Event2<ThreadBase, Exception>()

    val eventOkMessageReceived = Event2<ArduinoController, CommandType>()
    val eventErrorMessageReceived = Event2<ArduinoController, Command>()
    val eventMessageNotDecoded = Event2<ArduinoController, ByteArray>()

    val eventGetVoltageReceived = Event2<ArduinoController, Int>()

    init {
        connector.eventThreadStateChanged += { th, st -> eventThreadStateChanged.raise(th, st) }
        connector.eventThreadException += { th, ex -> eventThreadException.raise(th, ex) }
        connector.eventMessageReceived += this::onMessageReceived
        connector.eventMessageNotDecoded += { _, dt -> eventMessageNotDecoded.raise(this, dt) }
    }

    fun start() = connector.start()
    fun stop() = connector.stop()

    fun getVoltageAsync() {
        val command = Command(CommandType.GET_VOLTAGE)
        connector.sendCommand(command)
    }

    fun stopMotorsAsync() {
        val command = Command(CommandType.SET_MOTORS)
        connector.sendCommand(command)
    }

    fun setMotorsAsync(left: Int, right: Int) {
        val data = PayloadCodec.makeSetMotorsPayload(left, right)
        val command = Command(CommandType.SET_MOTORS, data)
        connector.sendCommand(command)
    }

    fun setLightsOffAsync() {
        val command = Command(CommandType.SET_LIGHT)
        connector.sendCommand(command)
    }

    fun setLightsAsync(color: Color) {
        val data = PayloadCodec.makeSetLightsPayload(color)
        val command = Command(CommandType.SET_LIGHT, data)
        connector.sendCommand(command)
    }

    fun setLightsMultipleAsync(vararg colors: Color) {
        val data = PayloadCodec.makeSetLightsPayloadMultiple(*colors)
        val command = Command(CommandType.SET_LIGHT, data)
        connector.sendCommand(command)
    }

    private fun onMessageReceived(connector: ArduinoConnector, command: Command) {
        when(command.commandType) {
            CommandType.OK -> processOkMessage(command)
            CommandType.GET_VOLTAGE -> processGetVoltage(command)
            else -> processOtherMessage(command)
        }
    }

    private fun processOkMessage(command: Command) {
        val okCommandType = CommandType.getByByteValueOrNull(command.data.firstOrNull()) ?: CommandType.NONE
        eventOkMessageReceived.raise(this, okCommandType)
    }

    private fun processGetVoltage(command: Command) {
        if(command.data.size == 2) {
            eventGetVoltageReceived.raise(this, PayloadCodec.getVoltageFromPayload(command.data))
        } else {
            eventErrorMessageReceived.raise(this, command)
        }
    }

    private fun processOtherMessage(command: Command) {
        eventErrorMessageReceived.raise(this, command)
    }
}