package su.barsuk.logging.writers

import su.barsuk.logging.LogWriter

abstract class LogWriterBase : LogWriter {
    protected fun formatMessage(dateTime: String, threadName: String, logLevel: String, message: String): String {
        return "[$dateTime] ($threadName) ${logLevel.padStart(5)}: $message"
    }
}