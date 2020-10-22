package su.barsuk.connectivity.threads

import jssc.SerialPortException
import jssc.SerialPortTimeoutException
import su.barsuk.connectivity.SerialPortConnector
import su.barsuk.common.events.Event1
import su.barsuk.common.threading.ThreadBase

internal class SerialPortReceiverThread(
        private val portContainer: SerialPortConnector,
        private val receiveTimeoutMillis: Int = 1000
) : ThreadBase(SerialPortReceiverThread::class.simpleName!!) {
    val eventBytesReceived = Event1<ByteArray>()
    val eventPortException = Event1<SerialPortException>()

    override fun onStart() {}

    override fun onCycle() {
        try {
            val port = portContainer.port
            if(port == null) {
                Thread.sleep(receiveTimeoutMillis.toLong())
                return
            }
            val bytesToRead = port.readBytes(1, receiveTimeoutMillis).first().toInt()
            val bytes = port.readBytes(bytesToRead, receiveTimeoutMillis)
            eventBytesReceived.raise(bytes)
        } catch (_: SerialPortTimeoutException) {
            // Do nothing
        } catch (ex: SerialPortException) {
            eventPortException.raise(ex)
        }
    }

    override fun onFinish() {}

    override fun onDispose() {
        eventBytesReceived.dispose()
        eventPortException.dispose()
    }
}
