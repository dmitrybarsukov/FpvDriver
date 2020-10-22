package su.barsuk.common.events

typealias Subscriber3<T1, T2, T3> = (T1, T2, T3) -> Unit

class Event3<T1, T2, T3> {
    private val subscribers = mutableListOf<Subscriber3<T1, T2, T3>>()

    @Synchronized
    fun subscribe(subscriber: Subscriber3<T1, T2, T3>) {
        if(!subscribers.contains(subscriber))
            subscribers.add(subscriber)
    }

    @Synchronized
    fun unsubscribe(subscriber: Subscriber3<T1, T2, T3>) {
        if(subscribers.contains(subscriber))
            subscribers.remove(subscriber)
    }

    @Synchronized
    fun raise(arg1: T1, arg2: T2, arg3: T3) {
        subscribers.forEach {
            it(arg1, arg2, arg3)
        }
    }

    @Synchronized
    fun dispose() {
        subscribers.clear()
    }

    operator fun plusAssign(subscriber: Subscriber3<T1, T2, T3>) = subscribe(subscriber)
    operator fun minusAssign(subscriber: Subscriber3<T1, T2, T3>) = unsubscribe(subscriber)
    operator fun invoke(arg1: T1, arg2: T2, arg3: T3) = raise(arg1, arg2, arg3)
}
