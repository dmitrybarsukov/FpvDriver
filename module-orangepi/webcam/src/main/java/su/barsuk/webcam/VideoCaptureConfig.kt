package su.barsuk.webcam

data class VideoCaptureConfig(
        val width: Int,
        val height: Int,
        val isColor: Boolean,
        val format: String,
        val codec: Int,
        val pixelFormat: Int,
        val bitrate: Int,
        val gopSize: Int
)
