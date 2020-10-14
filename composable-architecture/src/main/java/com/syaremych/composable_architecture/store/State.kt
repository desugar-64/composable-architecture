package com.syaremych.composable_architecture.store

import kotlin.reflect.KProperty

internal interface State<T> {
    val value: T
    companion object
}

@Suppress("NOTHING_TO_INLINE")
internal inline operator fun <T> State<T>.getValue(thisObj: Any?, property: KProperty<*>): T = value

internal interface MutableState<T> : State<T> {
    override var value: T
    companion object
}

@Suppress("NOTHING_TO_INLINE")
internal inline operator fun <T> MutableState<T>.setValue(thisObj: Any?, property: KProperty<*>, value: T) {
    this.value = value
}

internal class StateImpl<T>(initial: T) : State<T> {
    override val value: T = initial
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as StateImpl<*>

        if (value != other.value) return false

        return true
    }

    override fun hashCode(): Int {
        return value?.hashCode() ?: 0
    }


}

internal class MutableStateImpl<T>(initial: T)  : MutableState<T> {
    private var state: T = initial

    override var value: T
        get() = state
        set(value) {
            state = value
        }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MutableStateImpl<*>

        if (state != other.state) return false

        return true
    }

    override fun hashCode(): Int {
        return state?.hashCode() ?: 0
    }


}