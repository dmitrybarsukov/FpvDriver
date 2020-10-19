package su.barsuk.logging

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ConsoleLogWriter : LogWriterBase() {
    private val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")

    override fun writeLog(dateTime: LocalDateTime, threadName: String, logLevel: String, message: String) {
        val msg = formatMessage(
                dateTime.format(dateTimeFormatter),
                threadName,
                logLevel,
                message
        )
        println(msg)
    }
}