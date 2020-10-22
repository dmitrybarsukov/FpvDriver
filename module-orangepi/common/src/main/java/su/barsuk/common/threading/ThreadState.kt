package su.barsuk.common.threading

enum class ThreadState {
    NOT_STARTED,
    STARTING,
    RUNNING,
    ERROR,
    STOPPING,
    DISPOSING,
    DISPOSED
}