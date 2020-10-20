package su.barsuk.common.events

typealias Subscriber1<T1> = (T1) -> Unit

class Event1<T1> {
    private val subscribers = mutableListOf<Subscriber1<T1>>()

    @Synchronized
    fun subscribe(subscriber: Subscriber1<T1>) {
        if(!subscribers.contains(subscriber))
            subscribers.add(subscriber)
    }

    @Synchronized
    fun unsubscribe(subscriber: Subscriber1<T1>) {
        if(subscribers.contains(subscriber))
            subscribers.remove(subscriber)
    }

    @Synchronized
    fun raise(arg: T1) {
        subscribers.forEach {
            it(arg)
        }
    }

    operator fun plusAssign(subscriber: Subscriber1<T1>) = subscribe(subscriber)
    operator fun minusAssign(subscriber: Subscriber1<T1>) = unsubscribe(subscriber)
    operator fun invoke(arg: T1) = raise(arg)
}
