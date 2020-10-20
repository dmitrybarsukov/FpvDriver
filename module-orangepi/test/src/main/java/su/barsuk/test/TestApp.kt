package su.barsuk.test

import su.barsuk.arduinoconnector.ArduinoConnector
import su.barsuk.arduinoconnector.ArduinoController
import su.barsuk.arduinoconnector.protocol.Color
import su.barsuk.common.extensions.toHexString
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

fun goTests(controller: ArduinoController) {
    controller.getVoltageAsync()
    controller.setLightsAsync(Color.RED)
    controller.setMotorsAsync(255, 255)
    Thread.sleep(2000)
    controller.stopMotorsAsync()
    Thread.sleep(100)
}

fun main(args: Array<String>) {
    Log.writer = makeLogWriter("", "TestApp")
    Log.level = LogLevel.ALL
    // Here will be quick tests

    val port = ArduinoConnector.getPortNames().first()

    Log.debug("Starting")

    val controller = ArduinoController(port)
    controller.eventMessageNotDecoded += { _, bytes -> Log.warn("Not decoded [${bytes.toHexString()}]") }
    controller.eventOkMessageReceived += { _, ct -> Log.info("Received OK: $ct") }
    controller.eventErrorMessageReceived += { _, ct -> Log.error("Received ERROR: $ct") }
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