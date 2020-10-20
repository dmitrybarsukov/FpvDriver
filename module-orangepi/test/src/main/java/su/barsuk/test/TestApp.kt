package su.barsuk.test

import su.barsuk.logging.Log
import su.barsuk.logging.LogLevel
import su.barsuk.logging.LogWriter
import su.barsuk.logging.writers.AsyncLogWriter
import su.barsuk.logging.writers.BufferedFileLogWriter
import su.barsuk.logging.writers.ConsoleLogWriter
import su.barsuk.logging.writers.TeeLogWriter

fun makeLogWriter(path: String, appName: String): LogWriter {
    return TeeLogWriter(
            ConsoleLogWriter(),
            AsyncLogWriter(
                    BufferedFileLogWriter(path, appName)
            )
    )
}

fun main(args: Array<String>) {
    Log.writer = makeLogWriter("", "TestApp")
    Log.level = LogLevel.ALL
    // Here will be quick tests
}