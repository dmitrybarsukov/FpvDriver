package su.barsuk.logging

import java.time.LocalDateTime

interface LogWriter {
    fun writeLog(dateTime: LocalDateTime, threadName: String, logLevel: String, message: String)
}