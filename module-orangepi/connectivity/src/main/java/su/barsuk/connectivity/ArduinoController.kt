package su.barsuk.connectivity

import su.barsuk.connectivity.protocol.Color
import su.barsuk.connectivity.protocol.Message
import su.barsuk.connectivity.protocol.MessageType
import su.barsuk.connectivity.protocol.PayloadCodec
import su.barsuk.common.events.Event2
import su.barsuk.common.extensions.toHexString
import su.barsuk.common.threading.ThreadBase
import su.barsuk.common.threading.ThreadState

class ArduinoController(
        portName: String
) {
    private val connector = ArduinoConnector(portName)

    val eventThreadStateChanged = Event2<ThreadBase, ThreadState>()
    val eventThreadException = Event2<ThreadBase, Exception>()

    val eventOkReceived = Event2<ArduinoController, String>()
    val eventErrorReceived = Event2<ArduinoController, String>()

    val eventGetVoltageReceived = Event2<ArduinoController, Int>()

    init {
        connector.eventThreadStateChanged += { th, st -> eventThreadStateChanged.raise(th, st) }
        connector.eventThreadException += { th, ex -> eventThreadException.raise(th, ex) }
        connector.eventMessageReceived += this::onMessageReceived
        connector.eventMessageNotDecoded += this::onMessageNotDecoded
    }

    fun start() = connector.start()
    fun stop() = connector.stop()

    fun getVoltage() {
        val message = Message(MessageType.GET_VOLTAGE)
        connector.sendMessage(message)
    }

    fun stopMotors() {
        val message = Message(MessageType.SET_MOTORS)
        connector.sendMessage(message)
    }

    fun setMotors(left: Int, right: Int) {
        val data = PayloadCodec.makeSetMotorsPayload(left, right)
        val message = Message(MessageType.SET_MOTORS, data)
        connector.sendMessage(message)
    }

    fun setLightsOff() {
        val message = Message(MessageType.SET_LIGHT)
        connector.sendMessage(message)
    }

    fun setLights(color: Color) {
        val data = PayloadCodec.makeSetLightsPayload(color)
        val message = Message(MessageType.SET_LIGHT, data)
        connector.sendMessage(message)
    }

    fun setLightsMultiple(vararg colors: Color) {
        val data = PayloadCodec.makeSetLightsPayloadMultiple(*colors)
        val message = Message(MessageType.SET_LIGHT, data)
        connector.sendMessage(message)
    }

    private fun onMessageReceived(message: Message) {
        when(message.messageType) {
            MessageType.OK -> processOkMessage(message)
            MessageType.GET_VOLTAGE -> processGetVoltage(message)
            else -> processOtherMessage(message)
        }
    }

    private fun processOkMessage(message: Message) {
        val okMessageType = MessageType.getByByteValueOrNull(message.data.firstOrNull())
        val okMessage = "OK: $okMessageType"
        eventOkReceived.raise(this, okMessage)
    }

    private fun processGetVoltage(message: Message) {
        if(message.data.size == 2) {
            val voltage = PayloadCodec.getVoltageFromPayload(message.data)
            eventGetVoltageReceived.raise(this, voltage)
        } else {
            val errorMessage = "${MessageType.GET_VOLTAGE} wrong payload: [${message.data.toHexString()}]"
            eventErrorReceived.raise(this, errorMessage)
        }
    }

    private fun processOtherMessage(message: Message) {
        val errorMessage = "Error: $message"
        eventErrorReceived.raise(this, errorMessage)
    }

    private fun onMessageNotDecoded(data: ByteArray) {
        val errorMessage = "Not decoded: [${data.toHexString()}]"
        eventErrorReceived.raise(this, errorMessage)
    }

    companion object {
        fun getPortNames(): Array<String> {
            return SerialPortConnector.getPortNames()
        }
    }
}