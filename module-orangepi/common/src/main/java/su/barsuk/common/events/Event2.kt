package su.barsuk.common.events

typealias Subscriber2<T1, T2> = (T1, T2) -> Unit

class Event2<T1, T2> {
    private val subscribers = mutableListOf<Subscriber2<T1, T2>>()

    @Synchronized
    fun subscribe(subscriber: Subscriber2<T1, T2>) {
        if(!subscribers.contains(subscriber))
            subscribers.add(subscriber)
    }

    @Synchronized
    fun unsubscribe(subscriber: Subscriber2<T1, T2>) {
        if(subscribers.contains(subscriber))
            subscribers.remove(subscriber)
    }

    @Synchronized
    fun raise(arg1: T1, arg2: T2) {
        subscribers.forEach {
            it(arg1, arg2)
        }
    }

    @Synchronized
    fun dispose() {
        subscribers.clear()
    }

    operator fun plusAssign(subscriber: Subscriber2<T1, T2>) = subscribe(subscriber)
    operator fun minusAssign(subscriber: Subscriber2<T1, T2>) = unsubscribe(subscriber)
    operator fun invoke(arg1: T1, arg2: T2) = raise(arg1, arg2)
}
