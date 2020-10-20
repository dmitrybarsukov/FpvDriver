package su.barsuk.logging

import java.time.LocalDateTime

data class LogWriterItem(
        val dateTime: LocalDateTime,
        val threadName: String,
        val logLevel: String,
        val message: String
)