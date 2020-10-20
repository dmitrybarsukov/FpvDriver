package su.barsuk.common.threading

import su.barsuk.common.events.Event2

abstract class ThreadBase(
        private val name: String,
        private val priority: ThreadPriority = ThreadPriority.NORMAL,
        private val isDaemon: Boolean = false
) {
    val stateChanged = Event2<ThreadBase, ThreadState>()
    val exception = Event2<ThreadBase, Exception>()
    val uncaughtException = Event2<ThreadBase, Throwable>()

    private lateinit var thread: Thread
    private var isRunning: Boolean = true
    private var state: ThreadState = ThreadState.NOT_STARTED

    val id: Long get() = thread.id

    fun start() {
        if(state != ThreadState.NOT_STARTED)
            throw IllegalStateException("${this.javaClass.simpleName} can not be started twice")
        thread = Thread(::threadFunc, name)
        thread.priority = priority.value
        thread.isDaemon = isDaemon
        thread.setUncaughtExceptionHandler { _, throwable -> uncaughtException.raise(this, throwable) }
        thread.start()
    }

    fun stop() {
        if(state == ThreadState.NOT_STARTED)
            throw IllegalStateException("${this.javaClass.simpleName} was not started yet")
        isRunning = false
    }

    fun join() {
        thread.join()
    }

    fun join(millis: Long) {
        thread.join(millis)
    }

    fun join(millis: Long, nanos: Int) {
        thread.join(millis, nanos)
    }

    fun kill() {
        isRunning = false
        thread.interrupt()
    }

    abstract fun onStart()
    abstract fun onCycle()
    abstract fun onFinish()

    private fun threadFunc() {
        while(isRunning) {
            changeState(ThreadState.STARTING)
            try {
                onStart()
                changeState(ThreadState.RUNNING)
                while(isRunning) {
                    onCycle()
                }
            }
            catch (ex: Exception) {
                changeState(ThreadState.ERROR)
                exception.raise(this, ex)
            }
            finally {
                changeState(ThreadState.STOPPING)
                onFinish()
            }
        }
        changeState(ThreadState.STOPPED)
    }

    private fun changeState(newState: ThreadState) {
        if(state != newState){
            state = newState
            stateChanged.raise(this, state)
        }
    }

    companion object {
        fun joinAll(vararg threads: ThreadBase) {
            threads.forEach {
                it.join()
            }
        }

        fun joinAll(millis: Long, vararg threads: ThreadBase) {
            threads.forEach {
                it.join(millis)
            }
        }

        fun joinAll(millis: Long, nanos: Int, vararg threads: ThreadBase) {
            threads.forEach {
                it.join(millis, nanos)
            }
        }
    }
}