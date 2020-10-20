package su.barsuk.arduinoconnector

import jssc.SerialPort
import jssc.SerialPortException
import jssc.SerialPortList
import su.barsuk.arduinoconnector.protocol.Command
import su.barsuk.arduinoconnector.protocol.CommandCodec
import su.barsuk.arduinoconnector.threads.SerialPortReceiverThread
import su.barsuk.arduinoconnector.threads.SerialPortSenderThread
import su.barsuk.common.events.Event2
import su.barsuk.common.threading.ThreadBase
import su.barsuk.common.threading.ThreadState

class ArduinoConnector(
        portName: String
) {
    private val portContainer = SerialPortConnector(portName, SerialPort.BAUDRATE_115200)

    private val senderThread = SerialPortSenderThread(portContainer)
    private val receiverThread = SerialPortReceiverThread(portContainer)

    private var isRunning: Boolean? = null

    val eventMessageReceived = Event2<ArduinoConnector, Command>()
    val eventMessageNotDecoded = Event2<ArduinoConnector, ByteArray>()
    val eventThreadStateChanged = Event2<ThreadBase, ThreadState>()
    val eventThreadException = Event2<ThreadBase, Exception>()

    fun start() {
        if(isRunning != null)
            throw IllegalStateException("${ArduinoConnector::class.simpleName} can not be started twice")
        isRunning = true
        senderThread.eventStateChanged += { thread, state -> eventThreadStateChanged.raise(thread, state) }
        senderThread.eventException += { thread, ex -> eventThreadException.raise(thread, ex) }
        senderThread.eventPortException += this::onPortException

        receiverThread.eventStateChanged += { thread, state -> eventThreadStateChanged.raise(thread, state) }
        receiverThread.eventException += { thread, ex -> eventThreadException.raise(thread, ex) }
        receiverThread.eventPortException += this::onPortException
        receiverThread.eventBytesReceived += this::onBytesReceived

        portContainer.reconnect()
        senderThread.start()
        receiverThread.start()
    }

    fun stop() {
        if(isRunning == null)
            throw IllegalStateException("${ArduinoConnector::class.simpleName} was not started yet")
        isRunning = false
        senderThread.stop()
        receiverThread.stop()
        ThreadBase.joinAll(senderThread, receiverThread)
        portContainer.disconnect()
    }

    fun sendCommand(command: Command) {
        val bytes = CommandCodec.encode(command)
        senderThread.sendByteArray(bytes)
    }

    private fun onPortException(ex: SerialPortException) {
        portContainer.reconnect()
    }

    private fun onBytesReceived(bytes: ByteArray) {
        val command = CommandCodec.decode(bytes)
        if(command != null)
            eventMessageReceived.raise(this, command)
        else
            eventMessageNotDecoded.raise(this, bytes)
    }

    companion object {
        fun getPortNames(): Array<String> {
            return SerialPortList.getPortNames()
        }
    }
}