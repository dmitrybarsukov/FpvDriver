package su.barsuk.common.threading

enum class ThreadPriority(val value: Int) {
    LOWEST(1),
    IDLE(2),
    BACKGROUND(3),
    BELOW_NORMAL(4),
    NORMAL(5),
    ABOVE_NORMAL(6),
    FOREGROUND(7),
    IMPORTANT(8),
    HIGH(9),
    HIGHEST(10)
}