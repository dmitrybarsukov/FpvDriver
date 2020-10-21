package su.barsuk.arduinoconnector

import jssc.SerialPort
import jssc.SerialPortList

internal class SerialPortConnector(
        private val portName: String,
        private val baudRate: Int
) {
    var port: SerialPort? = null

    @Synchronized
    fun reconnect() {
        port?.closePort()
        port = SerialPort(portName)
        port?.run {
            openPort()
            setParams(baudRate, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE)
        }
    }

    @Synchronized
    fun disconnect() {
        port?.closePort()
        port = null
    }

    companion object {
        fun getPortNames(): Array<String> {
            return SerialPortList.getPortNames()
        }
    }
}
