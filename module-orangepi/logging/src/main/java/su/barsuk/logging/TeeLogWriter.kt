package su.barsuk.logging

import java.time.LocalDateTime

class TeeLogWriter(
        private val logWriters: Array<LogWriter>
) : LogWriter {
    override fun writeLog(dateTime: LocalDateTime, threadName: String, logLevel: String, message: String) {
        logWriters.forEach {
            it.writeLog(dateTime, threadName, logLevel, message)
        }
    }
}