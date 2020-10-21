package su.barsuk.test

import su.barsuk.connectivity.ArduinoController
import su.barsuk.connectivity.protocol.Color
import su.barsuk.logging.Log
import su.barsuk.logging.LogLevel
import su.barsuk.logging.LogWriter
import su.barsuk.logging.writers.BufferedFileLogWriter
import su.barsuk.logging.writers.ConsoleLogWriter
import su.barsuk.logging.writers.AsyncLogWriter
import su.barsuk.logging.writers.TeeLogWriter

fun makeLogWriter(path: String, appName: String): LogWriter {
    return TeeLogWriter(
            ConsoleLogWriter(),
            AsyncLogWriter(
                    BufferedFileLogWriter(path, appName)
            )
    )
}

fun goTests(controller: ArduinoController) {
    controller.getVoltage()
    controller.setLights(Color.RED)
    controller.setMotors(255, 255)
    Thread.sleep(2000)
    controller.stopMotors()
    Thread.sleep(100)
}

fun main(args: Array<String>) {
    Log.writer = makeLogWriter("", "TestApp")
    Log.level = LogLevel.ALL
    // Here will be quick tests

    val ports = ArduinoController.getPortNames()

    Log.debug("Starting")

    val controller = ArduinoController(ports.first())
    controller.eventErrorReceived += { _, err -> Log.error(err) }
    controller.eventOkReceived += { _, msg -> Log.debug(msg) }
    controller.eventGetVoltageReceived += { _, vl -> Log.info("Received voltage: $vl mv") }

    try{
        controller.start()
        goTests(controller)
    }
    finally {
        controller.stop()
    }

    Log.debug("Exiting")
    Log.finalizeLogs()
}