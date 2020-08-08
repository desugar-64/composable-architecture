package com.syaremych.composable_architecture.store

import android.os.Looper
import androidx.lifecycle.*

class LiveDataStore<Value : Any, Action : Any>(
    private val store: Store<Value, Action>,
    releaseStoreWith: LifecycleOwner
) : LiveData<Value>(),
    Store.Subscriber<Value> {

    init {
        val lifecycle = releaseStoreWith.lifecycle
        val releaseStoreObserver = object : LifecycleObserver {
            @OnLifecycleEvent(value = Lifecycle.Event.ON_DESTROY)
            fun onDestroy() {
                release()
                lifecycle.removeObserver(this)
            }
        }
        lifecycle.addObserver(releaseStoreObserver)
    }

    override fun onActive() {
        super.onActive()
        store.subscribe(this)
    }

    override fun onInactive() {
        super.onInactive()
        store.unsubscribe(this)
    }

    override fun render(value: Value) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            setValue(value)
        } else {
            postValue(value)
        }
    }

    fun send(action: Action) {
        store.send(action)
    }

    private fun release() {
        if (!hasObservers()) {
            // This means whatever was observing this LiveDataStore has died.
            // So we can release the Store and go to the r̶a̶p̶t̶u̶r̶e oblivion.
            store.release()
        }
    }
}

fun <Value : Any, Action : Any> Store<Value, Action>.asLiveData(releaseStoreWith: LifecycleOwner) =
    LiveDataStore(this, releaseStoreWith)