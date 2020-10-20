package su.barsuk.common.events

typealias Subscriber2<T1, T2> = (T1, T2) -> Unit

class Event2<T1, T2> {
    private val subscribers = mutableListOf<Subscriber2<T1, T2>>()

    @Synchronized
    fun subscribe(Subscriber2: Subscriber2<T1, T2>) {
        if(!subscribers.contains(Subscriber2))
            subscribers.add(Subscriber2)
    }

    @Synchronized
    fun unsubscribe(Subscriber2: Subscriber2<T1, T2>) {
        if(subscribers.contains(Subscriber2))
            subscribers.remove(Subscriber2)
    }

    @Synchronized
    fun raise(arg1: T1, arg2: T2) {
        subscribers.forEach {
            it(arg1, arg2)
        }
    }

    operator fun plusAssign(Subscriber2: Subscriber2<T1, T2>) = subscribe(Subscriber2)
    operator fun minusAssign(Subscriber2: Subscriber2<T1, T2>) = unsubscribe(Subscriber2)
    operator fun invoke(arg1: T1, arg2: T2) = raise(arg1, arg2)
}
