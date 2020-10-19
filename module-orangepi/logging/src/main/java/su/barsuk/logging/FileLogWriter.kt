package su.barsuk.logging

import java.io.File
import java.nio.charset.Charset
import java.nio.file.Path
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class FileLogWriter(
        private val path: String,
        private val appName: String,
        private val charset: Charset = Charsets.UTF_8
) : LogWriterBase() {

    private val dateTimeFileContentsFormatter = DateTimeFormatter.ofPattern("HH:mm:ss.SSS")
    private val dateTimeFileNameFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    override fun writeLog(dateTime: LocalDateTime, threadName: String, logLevel: String, message: String) {
        val msg = formatMessage(
                dateTime.format(dateTimeFileContentsFormatter),
                threadName,
                logLevel,
                message
        )

        val fileName = "$appName-${dateTime.format(dateTimeFileNameFormatter)}.log"
        val filePath = Path.of(path, fileName).toUri()

        try {
            File(filePath).appendText("$msg\n", charset)
        }
        catch (ex: Exception) {
            println("Logging failed: $ex")
        }
    }
}