package su.barsuk.common.events

typealias Subscriber3<T1, T2, T3> = (T1, T2, T3) -> Unit

class Event3<T1, T2, T3> {
    private val subscribers = mutableListOf<Subscriber3<T1, T2, T3>>()

    @Synchronized
    fun subscribe(Subscriber3: Subscriber3<T1, T2, T3>) {
        if(!subscribers.contains(Subscriber3))
            subscribers.add(Subscriber3)
    }

    @Synchronized
    fun unsubscribe(Subscriber3: Subscriber3<T1, T2, T3>) {
        if(subscribers.contains(Subscriber3))
            subscribers.remove(Subscriber3)
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

    operator fun plusAssign(Subscriber3: Subscriber3<T1, T2, T3>) = subscribe(Subscriber3)
    operator fun minusAssign(Subscriber3: Subscriber3<T1, T2, T3>) = unsubscribe(Subscriber3)
    operator fun invoke(arg1: T1, arg2: T2, arg3: T3) = raise(arg1, arg2, arg3)
}
