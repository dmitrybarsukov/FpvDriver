package su.barsuk.logging.writers

import su.barsuk.logging.LogWriterItem
import java.io.File
import java.nio.charset.Charset
import java.nio.file.Path
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

open class FileLogWriter(
        private val path: String,
        private val appName: String
) : LogWriterBase() {

    protected val dateTimeFileContentsFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss.SSS")
    protected val dateTimeFileNameFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    override fun writeLog(logWriterItem: LogWriterItem) {
        val msg = logWriterItem.run {
            formatMessage(
                    dateTimeFileContentsFormatter.format(dateTime),
                    threadName,
                    logLevel,
                    message
            )
        }

        val fileName = "$appName-${dateTimeFileNameFormatter.format(logWriterItem.dateTime)}.log"
        val filePath = Path.of(path, fileName).toUri()

        try {
            File(filePath).appendText("$msg\n")
        }
        catch (ex: Exception) {
            println("Logging failed: $ex")
        }
    }

    override fun finalizeLogs() {}
}