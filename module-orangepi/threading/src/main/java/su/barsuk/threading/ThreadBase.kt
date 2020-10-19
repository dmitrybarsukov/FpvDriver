package su.barsuk.threading

abstract class ThreadBase(
        val name: String
) : Runnable {
    private var thread: Thread? = null
    private var isRunning: Boolean = false;

    fun start() {
        if(thread != null)
            throw IllegalStateException("${this.javaClass.simpleName} can not be started twice")

    }

    fun stop() {

    }

    abstract fun onStart()
    abstract fun onCycle()
    abstract fun onFinish()

    private fun threadFunc() {

    }
}