package su.barsuk.logging

abstract class LogWriterBase : LogWriter {
    protected fun formatMessage(dateTime: String, threadName: String, logLevel: String, message: String): String {
        return "[$dateTime] ($threadName) ${logLevel.padStart(5)}: $message"
    }
}