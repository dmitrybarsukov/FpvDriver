package su.barsuk.logging

interface LogWriter {
    fun writeLog(logWriterItem: LogWriterItem)
    fun finalizeLogs()
}