package su.barsuk.test


import org.bytedeco.ffmpeg.global.avcodec
import org.bytedeco.ffmpeg.global.avutil
import org.bytedeco.javacv.FFmpegFrameGrabber
import org.bytedeco.javacv.Frame
import org.bytedeco.javacv.FrameGrabber
import org.bytedeco.javacv.OpenCVFrameConverter
import org.bytedeco.opencv.global.opencv_imgcodecs
import su.barsuk.logging.Log
import su.barsuk.logging.LogLevel
import su.barsuk.logging.LogWriter
import su.barsuk.logging.writers.AsyncLogWriter
import su.barsuk.logging.writers.BufferedFileLogWriter
import su.barsuk.logging.writers.ConsoleLogWriter
import su.barsuk.logging.writers.TeeLogWriter
import su.barsuk.webcam.VideoCaptureConfig
import su.barsuk.webcam.VideoCaptureThread
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.concurrent.TimeoutException


fun makeLogWriter(path: String, appName: String): LogWriter {
    return TeeLogWriter(
            ConsoleLogWriter(),
            AsyncLogWriter(
                    BufferedFileLogWriter(path, appName)
            )
    )
}

fun saveToFile(filename: String, frame: Frame) {
    val conv = OpenCVFrameConverter.ToMat()
    val mat = conv.convert(frame)
    try {
        opencv_imgcodecs.imwrite(filename, mat)
    }
    finally {
        mat.release()
    }
}

fun main() {
    Log.writer = makeLogWriter("log", "TestApp")
    Log.level = LogLevel.ALL
    Log.debug("Starting")
    avutil.av_log_set_level(avutil.AV_LOG_QUIET)
    val captureConfig = VideoCaptureConfig(
            1280, 720, true,
            "mpegts",
            avcodec.AV_CODEC_ID_H264,
            avutil.AV_PIX_FMT_YUV420P,
            10 * 1024 * 1024,
            10
    )
    val cameraNo = 2
    val captureThread = VideoCaptureThread(cameraNo, captureConfig)

    captureThread.eventStateChanged += { _, st -> Log.info("state changed to $st") }
    captureThread.eventException += { _, ex -> Log.error(ex) }

    val filename = "result.mpegts"

    FileOutputStream(filename).use {
        captureThread.eventBytesCaptured += {
            _, ba -> it.write(ba)
        }
        captureThread.start()
        Thread.sleep(15000)
        captureThread.stop()
        captureThread.join()
        it.flush()
    }

    Log.info("File $filename saved")

    FileInputStream(filename).use {
        Log.info("Starting FFmpegFrameGrabber")
        val grabber = FFmpegFrameGrabber(it).apply {
            format = "mpegts"
            imageMode = FrameGrabber.ImageMode.COLOR
        }


        try {
            grabber.start()
            var count = 0
            while(true) {
                val frame = grabber.grab()
                Log.info("Got frame ${count++}: ${frame.imageWidth}x${frame.imageHeight}")
                saveToFile("frame$count.jpg", frame)
            }
        }
        catch (ex: TimeoutException) {
            Log.info("TIMEOUT")
        }
        catch (ex: Exception) {
            Log.error(ex)
        }
        finally {
            grabber.stop()
        }
    }





    Log.debug("Exiting")
    Log.finalizeLogs()
}