package su.barsuk.webcam

import su.barsuk.common.events.Event1
import su.barsuk.common.validation.IntValidator
import java.io.IOException
import java.io.OutputStream

internal class NotifyingOutputStream : OutputStream() {

    val eventBytesReceived = Event1<ByteArray>()

    @Volatile
    private var isClosed = false

    private fun ensureOpen() {
        if (isClosed) {
            throw IOException("Stream closed")
        }
    }

    override fun write(b: Int) {
        ensureOpen()
        eventBytesReceived.raise(byteArrayOf(b.toByte()))
    }

    override fun write(b: ByteArray) {
        ensureOpen()
        eventBytesReceived.raise(b)
    }

    override fun write(b: ByteArray, off: Int, len: Int) {
        IntValidator(off, "offset").checkInRange(b.indices)
        IntValidator(off + len - 1, "offsetEnd").checkInRange(b.indices)
        val cropped = b.sliceArray(off until (off + len))
        eventBytesReceived.raise(cropped)
    }

    override fun close() {
        isClosed = true
        eventBytesReceived.dispose()
    }
}