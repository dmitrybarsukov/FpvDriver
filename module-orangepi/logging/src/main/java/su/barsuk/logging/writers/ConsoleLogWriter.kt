package su.barsuk.logging.writers

import su.barsuk.logging.LogWriterItem
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ConsoleLogWriter : LogWriterBase() {
    private val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")

    override fun writeLog(logWriterItem: LogWriterItem) {
        val msg = logWriterItem.run {
            formatMessage(
                    dateTimeFormatter.format(dateTime),
                    threadName,
                    logLevel,
                    message
            )
        }
        println(msg)
    }

    override fun finalizeLogs() {}
}