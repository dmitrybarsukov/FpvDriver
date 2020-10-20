package su.barsuk.common.events

typealias Subscriber<T> = (T) -> Unit

class Event<T> {
    private val subscribers = mutableListOf<Subscriber<T>>()

    @Synchronized
    fun subscribe(subscriber: Subscriber<T>) {
        if(!subscribers.contains(subscriber))
            subscribers.add(subscriber)
    }

    @Synchronized
    fun unsubscribe(subscriber: Subscriber<T>) {
        if(subscribers.contains(subscriber))
            subscribers.remove(subscriber)
    }

    @Synchronized
    fun raise(arg: T) {
        subscribers.forEach {
            it(arg)
        }
    }

    operator fun plusAssign(subscriber: Subscriber<T>) = subscribe(subscriber)
    operator fun minusAssign(subscriber: Subscriber<T>) = unsubscribe(subscriber)
    operator fun invoke(arg: T) = raise(arg)
}
