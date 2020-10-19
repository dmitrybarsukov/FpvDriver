package su.barsuk.test

import su.barsuk.logging.*
import java.io.InvalidClassException

fun main(args: Array<String>) {
    val path = ""
    val appName = "TestApp"
    Log.writer = TeeLogWriter(arrayOf(ConsoleLogWriter(), FileLogWriter(path, appName)))
    //Log.level = LogLevel.INFO
    Log.debug("some debug message")
    Log.info("information message")
    Log.warn("warning message")
    Log.error("error message", InvalidClassException("Exception message"))
    Log.fatal("FUCKING CRAZY ERROR")
}