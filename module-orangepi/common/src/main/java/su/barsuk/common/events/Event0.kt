package su.barsuk.common.events

typealias Subscriber0 = () -> Unit

class Event0 {
    private val subscribers = mutableListOf<Subscriber0>()

    @Synchronized
    fun subscribe(subscriber: Subscriber0) {
        if(!subscribers.contains(subscriber))
            subscribers.add(subscriber)
    }

    @Synchronized
    fun unsubscribe(subscriber: Subscriber0) {
        if(subscribers.contains(subscriber))
            subscribers.remove(subscriber)
    }

    @Synchronized
    fun raise() {
        subscribers.forEach {
            it.invoke()
        }
    }

    @Synchronized
    fun dispose() {
        subscribers.clear()
    }

    operator fun plusAssign(subscriber: Subscriber0) = subscribe(subscriber)
    operator fun minusAssign(subscriber: Subscriber0) = unsubscribe(subscriber)
    operator fun invoke() = raise()
}
