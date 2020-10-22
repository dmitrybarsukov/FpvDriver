package su.barsuk.test


import org.bytedeco.ffmpeg.global.avcodec
import org.bytedeco.ffmpeg.global.avutil
import su.barsuk.logging.Log
import su.barsuk.logging.LogLevel
import su.barsuk.logging.LogWriter
import su.barsuk.logging.writers.AsyncLogWriter
import su.barsuk.logging.writers.BufferedFileLogWriter
import su.barsuk.logging.writers.ConsoleLogWriter
import su.barsuk.logging.writers.TeeLogWriter
import su.barsuk.webcam.VideoCaptureConfig
import su.barsuk.webcam.VideoCaptureThread


fun makeLogWriter(path: String, appName: String): LogWriter {
    return TeeLogWriter(
            ConsoleLogWriter(),
            AsyncLogWriter(
                    BufferedFileLogWriter(path, appName)
            )
    )
}

fun main() {
    Log.writer = makeLogWriter("log", "TestApp")
    Log.level = LogLevel.ALL
    Log.debug("Starting")
    //avutil.av_log_set_level(avutil.AV_LOG_QUIET)
    val captureConfig = VideoCaptureConfig(avcodec.AV_CODEC_ID_H264, avutil.AV_PIX_FMT_YUV420P, 10 * 1024 * 1024)
    val cameraNo = 2
    val captureThread = VideoCaptureThread(cameraNo, captureConfig)

    captureThread.eventStateChanged += { _, st -> Log.info("state changed to $st") }
    captureThread.eventException += { _, ex -> Log.error(ex) }
    captureThread.eventBytesCaptured += { _, ba -> Log.info("Data: ${ba.size} bytes") }

    captureThread.start()
    Thread.sleep(10000)
    captureThread.stop()
    captureThread.join()

    Log.debug("Exiting")
    Log.finalizeLogs()
}