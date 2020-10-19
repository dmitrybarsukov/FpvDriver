package su.barsuk.logging

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Log private constructor() {
    companion object {
        var level: LogLevel = LogLevel.ALL
        var writer: LogWriter? = null

        fun fatal(message: String) = writeLog(LogLevel.FATAL, message)
        fun fatal(exception: Exception) = writeLog(LogLevel.FATAL, formatException(exception))
        fun fatal(message: String, exception: Exception)
                = writeLog(LogLevel.FATAL, "$message: ${formatException(exception)}")

        fun error(message: String) = writeLog(LogLevel.ERROR, message)
        fun error(exception: Exception) = writeLog(LogLevel.ERROR, formatException(exception))
        fun error(message: String, exception: Exception)
                = writeLog(LogLevel.ERROR, "$message: ${formatException(exception)}")

        fun warn(message: String) = writeLog(LogLevel.WARN, message)

        fun info(message: String) = writeLog(LogLevel.INFO, message)

        fun debug(message: String) = writeLog(LogLevel.DEBUG, message)

        private fun writeLog(messageLogLevel: LogLevel, message: String) {
            if(level < messageLogLevel)
                return
            writer?.writeLog(
                    LocalDateTime.now(),
                    Thread.currentThread().name,
                    messageLogLevel.name,
                    message
            )
        }

        private fun formatException(exception: Exception): String {
            return "${exception.javaClass.simpleName}: ${exception.message}"
        }
    }
}