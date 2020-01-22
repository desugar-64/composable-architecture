package com.sergeyfitis.moviekeeper.statemanagement.store

import androidx.lifecycle.LiveData

class LiveDataStore<Value, Action>(private val store: Store<Value, Action>) : LiveData<Value>(), Store.Subscriber<Value> {

    override fun onActive() {
        super.onActive()
        store.subscribe(this)
    }

    override fun onInactive() {
        super.onInactive()
        store.unsubscribe(this)
    }

    override fun render(value: Value) {
        postValue(value)
    }

    fun send(action: Action) {
        store.send(action)
    }
}

fun <Value, Action> Store<Value, Action>.asLiveData() = LiveDataStore(this)