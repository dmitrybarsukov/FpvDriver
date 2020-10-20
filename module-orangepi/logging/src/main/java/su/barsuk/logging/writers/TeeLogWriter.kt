package su.barsuk.logging.writers

import su.barsuk.logging.LogWriter
import su.barsuk.logging.LogWriterItem

class TeeLogWriter(
        vararg logWriters: LogWriter
) : LogWriter {
    private val innerWriters = logWriters

    override fun writeLog(logWriterItem: LogWriterItem) {
        innerWriters.forEach {
            it.writeLog(logWriterItem)
        }
    }

    override fun finalizeLogs() {
        innerWriters.forEach {
            it.finalizeLogs()
        }
    }
}