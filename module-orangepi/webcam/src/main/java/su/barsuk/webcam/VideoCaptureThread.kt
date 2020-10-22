package su.barsuk.webcam

import org.bytedeco.javacv.FFmpegFrameRecorder
import org.bytedeco.javacv.FrameGrabber
import org.bytedeco.javacv.OpenCVFrameGrabber
import su.barsuk.common.events.Event2
import su.barsuk.common.threading.ThreadBase

class VideoCaptureThread(
        private val cameraDeviceNumber: Int,
        private val config: VideoCaptureConfig
) : ThreadBase(VideoCaptureThread::class.simpleName!!) {
    private lateinit var grabber: OpenCVFrameGrabber
    private lateinit var recorder: FFmpegFrameRecorder
    private lateinit var stream: NotifyingOutputStream

    val eventBytesCaptured = Event2<VideoCaptureThread, ByteArray>()

    override fun onStart() {
        stream = makeNewStream()
        grabber = OpenCVFrameGrabber(cameraDeviceNumber).apply {
            imageWidth = config.width
            imageHeight = config.height
            imageMode = if(config.isColor) FrameGrabber.ImageMode.COLOR else FrameGrabber.ImageMode.GRAY
        }
        grabber.start()
        val image = grabber.grab()
        recorder = FFmpegFrameRecorder(
                stream,
                image.imageWidth,
                image.imageHeight
        ).apply {
            format = config.format
            videoCodec = config.codec
            pixelFormat = config.pixelFormat
            videoBitrate = config.bitrate
            gopSize = config.gopSize
        }
        recorder.start()
    }

    override fun onCycle() {
        val image = grabber.grab()
        recorder.record(image)
    }

    override fun onFinish() {
        if(this::grabber.isInitialized) grabber.close()
        if(this::recorder.isInitialized) recorder.close()
        if(this::stream.isInitialized) stream.close()
    }

    override fun onDispose() {
        eventBytesCaptured.dispose()
    }

    private fun makeNewStream(): NotifyingOutputStream {
        if(this::stream.isInitialized) stream.close()
        stream = NotifyingOutputStream()
        stream.eventBytesReceived += { ba -> eventBytesCaptured.raise(this, ba) }
        return stream
    }
}