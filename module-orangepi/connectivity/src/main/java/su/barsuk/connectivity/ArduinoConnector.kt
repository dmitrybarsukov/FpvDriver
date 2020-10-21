package su.barsuk.connectivity

import jssc.SerialPort
import jssc.SerialPortException
import su.barsuk.connectivity.protocol.Message
import su.barsuk.connectivity.protocol.MessageCodec
import su.barsuk.connectivity.threads.SerialPortReceiverThread
import su.barsuk.connectivity.threads.SerialPortSenderThread
import su.barsuk.common.events.Event1
import su.barsuk.common.events.Event2
import su.barsuk.common.threading.ThreadBase
import su.barsuk.common.threading.ThreadState

internal class ArduinoConnector(
        portName: String
) {
    private val portContainer = SerialPortConnector(portName, SerialPort.BAUDRATE_115200)

    private val senderThread = SerialPortSenderThread(portContainer)
    private val receiverThread = SerialPortReceiverThread(portContainer)

    private var isRunning: Boolean? = null

    val eventMessageReceived = Event1<Message>()
    val eventMessageNotDecoded = Event1<ByteArray>()
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

    fun sendMessage(message: Message) {
        val bytes = MessageCodec.encode(message)
        senderThread.sendByteArray(bytes)
    }

    private fun onPortException(ex: SerialPortException) {
        portContainer.reconnect()
    }

    private fun onBytesReceived(bytes: ByteArray) {
        val message = MessageCodec.decode(bytes)
        if(message != null)
            eventMessageReceived.raise(message)
        else
            eventMessageNotDecoded.raise(bytes)
    }
}