package com.sergeyfitis.moviekeeper.statemanagement.store

typealias Effect<Action> = () -> Action?
typealias Reduced<Value, Action> = List<Pair<Value, Effect<Action>>>
typealias Reducer<Value, Action> = (value: Value, action: Action) -> Reduced<Value, Action>

@Suppress("NOTHING_TO_INLINE")
inline fun <Action> noEffect(): Effect<Action> = { null }
fun <Action> effectOf(action: Action): Effect<Action> = { action }
@Suppress("NOTHING_TO_INLINE")
inline fun <Value, Action> reduced(value: Value, noinline effect: Effect<Action>): Reduced<Value, Action> =
    listOf(value to effect)

class Store<Value, Action>(
    private val initialState: Value,
    private val reducer: Reducer<Value, Action>
) {
    interface Subscriber<Value> {
        fun render(value: Value)
    }

    var value: Value = initialState
        private set

    private val subscribers: MutableSet<Subscriber<Value>> = HashSet()

    fun subscribe(subscriber: Subscriber<Value>) {
        subscribers.add(subscriber)
        subscriber.render(value)
    }

    fun unsubscribe(subscriber: Subscriber<Value>) {
        subscribers.remove(subscriber)
    }

    fun send(action: Action) {
        val result = reducer(value, action)
        for ((newValue, effect) in result) {
            this.value = newValue
            effect()?.let(this::send)
        }
        subscribers.forEach { it.render(value) }
    }

    fun <LocalValue, LocalAction> view(
        toLocalValue: (Value) -> LocalValue,
        toGlobalAction: (LocalAction) -> Action
    ): Store<LocalValue, LocalAction> {
        val localStore = Store(
            initialState = toLocalValue(value),
            reducer = { _, localAction: LocalAction ->
                this.send(toGlobalAction(localAction))
                val localValue = toLocalValue(value)
                return@Store reduced(localValue, noEffect())
            }
        )

        subscribe(object : Subscriber<Value> {
            override fun render(value: Value) {
                localStore.value = toLocalValue(value)
            }
        })
        return localStore
    }
}
