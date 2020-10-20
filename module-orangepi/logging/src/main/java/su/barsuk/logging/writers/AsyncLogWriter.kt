package su.barsuk.logging.writers

import su.barsuk.logging.LogWriter
import su.barsuk.logging.LogWriterItem
import java.lang.Exception
import java.time.LocalDateTime
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.TimeUnit

class AsyncLogWriter(
        private val innerWriter: LogWriter
): LogWriter {
    private val thread = Thread(::logWriterThreadFunc, AsyncLogWriter::class.simpleName)
    private val messageQueue = LinkedBlockingQueue<LogWriterItem>()
    private var isRunning: Boolean = true

    init {
        thread.start()
    }

    override fun writeLog(logWriterItem: LogWriterItem) {
        messageQueue.put(logWriterItem)
    }

    override fun finalizeLogs() {
        isRunning = false
    }

    private fun logWriterThreadFunc() {
        while (isRunning || messageQueue.size > 0) {
            try {
                val item = messageQueue.poll(1, TimeUnit.SECONDS)
                item?.run { innerWriter.writeLog(item) }
            }
            catch (ex: Exception) {
                println("Logging failed: $ex")
            }
        }
        innerWriter.finalizeLogs()
    }
}