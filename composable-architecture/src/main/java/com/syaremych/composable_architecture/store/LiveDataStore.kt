package com.syaremych.composable_architecture.store

import androidx.lifecycle.LiveData

class LiveDataStore<Value, Action>(private val store: Store<Value, Action>) : LiveData<Value>(),
    Store.Subscriber<Value> {

    override fun onActive() {
        super.onActive()
        store.subscribe(this)
    }

    override fun onInactive() {
        super.onInactive()
        store.unsubscribe(this)
        if (!hasObservers()) {
            // TODO: this means whatever was observing this LiveDataStore has died. So we can release the Store and go to the r̶a̶p̶t̶u̶r̶e oblivion.
            store.release()
        }
    }

    override fun render(value: Value) {
        postValue(value)
    }

    fun send(action: Action) {
        store.send(action)
    }
}

fun <Value, Action> Store<Value, Action>.asLiveData() =
    LiveDataStore(this)