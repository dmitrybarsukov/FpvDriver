package su.barsuk.common.threading

import su.barsuk.common.events.Event2

abstract class ThreadBase(
        val name: String,
        private val timeoutOnFail: Long = 1000,
        private val priority: ThreadPriority = ThreadPriority.NORMAL,
        private val isDaemon: Boolean = false
) {
    val eventStateChanged = Event2<ThreadBase, ThreadState>()
    val eventException = Event2<ThreadBase, Exception>()

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

    protected abstract fun onStart()
    protected abstract fun onCycle()
    protected abstract fun onFinish()
    protected abstract fun onDispose()

    private fun threadFunc() {
        while(isRunning) {
            var restarting = false
            changeState(ThreadState.STARTING)
            try {
                onStart()
                changeState(ThreadState.RUNNING)
                while(isRunning) {
                    onCycle()
                }
            }
            catch (ex: Exception) {
                restarting = true
                changeState(ThreadState.ERROR)
                eventException.raise(this, ex)
            }

            try {
                changeState(ThreadState.STOPPING)
                onFinish()
            }
            catch (ex: Exception) {
                restarting = true
                changeState(ThreadState.ERROR)
                eventException.raise(this, ex)
            }

            if(restarting)
            {
                Thread.sleep(timeoutOnFail)
            }
        }
        changeState(ThreadState.DISPOSING)
        try {
            onDispose()
        }
        catch (ex: Exception) {
            eventException.raise(this, ex)
        }
        changeState(ThreadState.DISPOSED)
        eventException.dispose()
        eventStateChanged.dispose()
    }

    private fun changeState(newState: ThreadState) {
        if(state != newState){
            state = newState
            eventStateChanged.raise(this, state)
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