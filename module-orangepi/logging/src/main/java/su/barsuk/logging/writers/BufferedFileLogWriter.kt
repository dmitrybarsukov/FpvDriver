package su.barsuk.logging.writers

import su.barsuk.logging.LogWriterItem
import java.io.File
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*

class BufferedFileLogWriter(
        private val path: String,
        private val appName: String,
        private val bufferSize: Int = 50
) : FileLogWriter(path, appName) {

    private val messageBuffer = LinkedList<LogWriterItem>()

    override fun writeLog(logWriterItem: LogWriterItem) {
        messageBuffer.add(logWriterItem)

        if(messageBuffer.size >= bufferSize) {
            flushBuffer()
        }
    }

    override fun finalizeLogs() {
        if(messageBuffer.size > 0) {
            flushBuffer()
        }
    }

    private fun flushBuffer() {
        val fileName = "$appName-${dateTimeFileNameFormatter.format(messageBuffer.first.dateTime)}.log"
        val filePath = Paths.get(path, fileName).toUri()

        val text = messageBuffer.map {
            it.run {
                formatMessage(
                        dateTimeFileContentsFormatter.format(dateTime),
                        threadName,
                        logLevel,
                        message
                )
            }
        }.joinToString("\n")
        messageBuffer.clear()
        try {
            File(filePath).appendText("$text\n")
        }
        catch (ex: Exception) {
            println("Logging failed: $ex")
        }
     }
}