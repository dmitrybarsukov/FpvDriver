package su.barsuk.arduinoconnector.threads

import jssc.SerialPortException
import su.barsuk.arduinoconnector.SerialPortConnector
import su.barsuk.common.events.Event1
import su.barsuk.common.threading.ThreadBase
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.TimeUnit

internal class SerialPortSenderThread(
        private val portContainer: SerialPortConnector
) : ThreadBase(SerialPortSenderThread::class.simpleName!!) {
    private val byteArrayQueue = LinkedBlockingQueue<ByteArray>()

    val eventPortException = Event1<SerialPortException>()

    fun sendByteArray(byteArray: ByteArray) = byteArrayQueue.put(byteArray)

    override fun onStart() {}

    override fun onCycle() {
        try {
            val bytes = byteArrayQueue.poll(1, TimeUnit.SECONDS)
            bytes?.run {
                val packet = byteArrayOf(this.size.toByte(), *this)
                portContainer.port?.writeBytes(packet)
            }
        }
        catch (ex: SerialPortException) {
            eventPortException.raise(ex)
        }

    }

    override fun onFinish() {}
}
