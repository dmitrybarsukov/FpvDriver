package su.barsuk.arduinoconnector.threads

import jssc.SerialPortException
import jssc.SerialPortTimeoutException
import su.barsuk.arduinoconnector.SerialPortConnector
import su.barsuk.common.events.Event1
import su.barsuk.common.threading.ThreadBase

internal class SerialPortReceiverThread(
        private val portContainer: SerialPortConnector,
        private val receiveTimeoutMillis: Int = 10
) : ThreadBase(SerialPortReceiverThread::class.simpleName!!) {
    private val receiveBuffer = ArrayList<Byte>()

    val eventBytesReceived = Event1<ByteArray>()
    val eventPortException = Event1<SerialPortException>()

    override fun onStart() {}

    override fun onCycle() {
        try {
            val byte = portContainer.port?.readBytes(1, receiveTimeoutMillis)?.first()
            // TODO implement protocol to allow continuous transmission
            if(byte != null)
                receiveBuffer.add(byte)
        } catch (_: SerialPortTimeoutException) {
            if(receiveBuffer.isEmpty())
                return
            val bytes = receiveBuffer.toByteArray()
            receiveBuffer.clear()
            eventBytesReceived.raise(bytes)
        } catch (ex: SerialPortException) {
            receiveBuffer.clear()
            eventPortException.raise(ex)
        }
    }

    override fun onFinish() {}

}
