package com.syaremych.composable_architecture.store

import com.nhaarman.mockitokotlin2.mock
import com.syaremych.composable_architecture.prelude.types.Lens
import com.syaremych.composable_architecture.prelude.types.Option.Companion.empty
import com.syaremych.composable_architecture.prelude.types.Prism
import com.syaremych.composable_architecture.prelude.types.toOption
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import java.util.concurrent.CountDownLatch

@RunWith(MockitoJUnitRunner::class)
class StoreTest {

    data class IntState(val fst: Int, val snd: Int, val trd: Int)
    sealed class Act {
        object Inc : Act() {
            override fun toString() = "Act.Inc"
        }

        object IncFst : Act() {
            override fun toString() = "Act.IncFst"
        }

        object IncSnd : Act() {
            override fun toString() = "Act.IncSend"
        }

        object IncTrd : Act() {
            override fun toString() = "Act.IncTrd"
        }
    }

    private val effectsCount = 50

    @OptIn(ExperimentalStdlibApi::class)
    private val intReducer = Reducer<IntState, Act, Unit> { intState, action, _ ->
        when (action) {
            Act.Inc -> reduced(
                intState,
                Effect.merge<Act>(
                    generateSequence<Effect<Act>> {
                        flow<Act> {
                            emit(Act.IncFst)
                            println("Effect Act.IncFst, thread = ${threadName()}")
                        }.eraseToEffect()
                    }
                        .take(effectsCount)
                        .toList() +
                            generateSequence<Effect<Act>> {
                                flow<Act> {
                                    emit(Act.IncSnd)
                                    println("Effect Act.IncFst, thread = ${threadName()}")
                                }.eraseToEffect()
                            }
                                .take(effectsCount)
                                .toList()
                ).runOn(Dispatchers.Default)
            )
            Act.IncFst -> reduced(intState.copy(fst = intState.fst + 1))
            Act.IncSnd -> reduced(intState.copy(snd = intState.snd + 1))
            Act.IncTrd -> reduced(intState.copy(trd = intState.trd + 1))
        }
    }

    private fun threadName() = "${Thread.currentThread().name}:${Thread.currentThread().id}"

    private val store = Store.init<IntState, Act, Unit>(
        initialState = IntState(0, 0, 0),
        reducer = intReducer,
        environment = Unit,
        storeDispatcher = Dispatchers.Unconfined
    )


    @Before
    fun setUp() = runBlocking {
    }

    @Test
    fun store_MassiveParallelUpdate() = runBlocking {
        val expectedState = IntState(0, 0, 0)
        assertEquals(expectedState, store.stateHolder.value)
        store.send(Act.Inc)
        delay(500)
        assertEquals(IntState(50, 50, 0), store.stateHolder.value)
    }

    @Test
    fun store_stateUpdate() {
        val state = IntState(0, 0, 0)
        val reducer = Reducer<IntState, Act, Unit> { intState, act, _ ->
            when (act) {
                Act.Inc -> reduced(
                    value = intState.copy(
                        fst = intState.fst + 1,
                        snd = intState.snd + 1,
                        trd = intState.trd + 1
                    ),
                    effect = Effect.none()
                )
                Act.IncFst -> reduced(
                    value = intState.copy(
                        fst = intState.fst + 1
                    ),
                    effect = Effect.none()
                )
                Act.IncSnd -> reduced(
                    value = intState.copy(
                        snd = intState.snd + 1
                    ),
                    effect = Effect.none()
                )
                Act.IncTrd -> reduced(
                    value = intState.copy(
                        trd = intState.trd + 1
                    ),
                    effect = Effect.none()
                )
            }
        }
        val store = Store.init(
            initialState = state,
            reducer = reducer,
            environment = Unit,
            storeDispatcher = Dispatchers.Unconfined
        )

        assertEquals(state, store.stateHolder.value)

        store.send(Act.IncFst)
        assertEquals(state.copy(fst = 1), store.stateHolder.value)
        assertTrue(store.stateHolder.value.snd == 0)
        assertTrue(store.stateHolder.value.trd == 0)

        store.send(Act.IncSnd)
        assertEquals(state.copy(fst = 1, snd = 1), store.stateHolder.value)
        assertTrue(store.stateHolder.value.trd == 0)

        store.send(Act.IncTrd)
        assertEquals(state.copy(fst = 1, snd = 1, trd = 1), store.stateHolder.value)

        store.send(Act.IncFst)
        store.send(Act.IncFst)
        assertEquals(state.copy(fst = 3, snd = 1, trd = 1), store.stateHolder.value)
    }

    @Test
    fun store_pullbackStateUpdate() {
        class FstAction
        class SndAction
        class TrdAction

        data class Fst(val count: Int)
        data class Snd(val count: Int)
        data class Trd(val count: Int)

        val state = IntState(0, 0, 0)

        val fstReducer = Reducer<Fst, FstAction, Unit> { fst, _, _ ->
            reduced(fst.copy(count = fst.count + 1), Effect.none())
        }
        val sndReducer = Reducer<Snd, SndAction, Unit> { snd, _, _ ->
            reduced(snd.copy(count = snd.count + 1), Effect.none())
        }
        val trdReducer = Reducer<Trd, TrdAction, Unit> { trd, _, _ ->
            reduced(trd.copy(count = trd.count + 1), Effect.none())
        }
        val reducer = Reducer.combine<IntState, Act, Unit>(
            fstReducer.pullback(
                value = Lens(
                    get = { intState -> Fst(intState.fst) },
                    set = { intState, fst -> intState.copy(fst = fst.count) }
                ),
                action = Prism(
                    get = { act -> if (act == Act.IncFst) FstAction().toOption() else empty() },
                    reverseGet = { Act.IncFst }
                ),
                environment = { Unit }
            ),
            sndReducer.pullback(
                value = Lens(
                    get = { intState -> Snd(intState.snd) },
                    set = { intState, snd -> intState.copy(snd = snd.count) }
                ),
                action = Prism(
                    get = { act -> if (act == Act.IncSnd) SndAction().toOption() else empty() },
                    reverseGet = { Act.IncSnd }
                ),
                environment = { Unit }
            ),
            trdReducer.pullback(
                value = Lens(
                    get = { intState -> Trd(intState.trd) },
                    set = { intState, trd -> intState.copy(trd = trd.count) }
                ),
                action = Prism(
                    get = { act -> if (act == Act.IncTrd) TrdAction().toOption() else empty() },
                    reverseGet = { Act.IncTrd }
                ),
                environment = { Unit }
            )
        )

        val store = Store.init(state, reducer, Unit, Dispatchers.Unconfined)

        assertEquals(state, store.stateHolder.value)

        store.send(Act.Inc)
        assertEquals(state, store.stateHolder.value) // nothing should change

        store.send(Act.IncFst)
        assertEquals(state.copy(fst = 1, snd = 0, trd = 0), store.stateHolder.value)

        store.send(Act.IncFst)
        assertEquals(state.copy(fst = 2, snd = 0, trd = 0), store.stateHolder.value)

        store.send(Act.IncSnd)
        assertEquals(state.copy(fst = 2, snd = 1, trd = 0), store.stateHolder.value)

        store.send(Act.IncTrd)
        assertEquals(state.copy(fst = 2, snd = 1, trd = 1), store.stateHolder.value)

    }

    @Test
    fun store_scopeStateUpdate() {
        class FstAction
        class SndAction
        class TrdAction

        data class Fst(val count: Int)
        data class Snd(val count: Int)
        data class Trd(val count: Int)

        val state = IntState(0, 0, 0)

        val fstReducer = Reducer<Fst, FstAction, Unit> { fst, _, _ ->
            reduced(fst.copy(count = fst.count + 1), Effect.none())
        }
        val sndReducer = Reducer<Snd, SndAction, Unit> { snd, _, _ ->
            reduced(snd.copy(count = snd.count + 1), Effect.none())
        }
        val trdReducer = Reducer<Trd, TrdAction, Unit> { trd, _, _ ->
            reduced(trd.copy(count = trd.count + 1), Effect.none())
        }
        val intStateToFst = Lens<IntState, Fst>(
            get = { intState -> Fst(intState.fst) },
            set = { intState, fst -> intState.copy(fst = fst.count) }
        )
        val actToFstAction = Prism<Act, FstAction>(
            get = { act -> if (act == Act.IncFst) FstAction().toOption() else empty() },
            reverseGet = { Act.IncFst }
        )

        val intStateToSnd = Lens<IntState, Snd>(
            get = { intState -> Snd(intState.snd) },
            set = { intState, snd -> intState.copy(snd = snd.count) }
        )
        val actToSndAction = Prism<Act, SndAction>(
            get = { act -> if (act == Act.IncSnd) SndAction().toOption() else empty() },
            reverseGet = { Act.IncSnd }
        )
        val intStateToTrd = Lens<IntState, Trd>(
            get = { intState -> Trd(intState.trd) },
            set = { intState, trd -> intState.copy(trd = trd.count) }
        )
        val actToTrdAction = Prism<Act, TrdAction>(
            get = { act -> if (act == Act.IncTrd) TrdAction().toOption() else empty() },
            reverseGet = { Act.IncTrd }
        )
        val reducer = Reducer.combine<IntState, Act, Unit>(
            fstReducer.pullback(
                value = intStateToFst,
                action = actToFstAction,
                environment = { Unit }
            ),
            sndReducer.pullback(
                value = intStateToSnd,
                action = actToSndAction,
                environment = { Unit }
            ),
            trdReducer.pullback(
                value = intStateToTrd,
                action = actToTrdAction,
                environment = { Unit }
            )
        )

        val store = Store.init(state, reducer, Unit, Dispatchers.Unconfined)

        assertEquals(state, store.stateHolder.value)

        val fstStore = store.scope<Fst, FstAction>(
            toLocalValue = intStateToFst::get,
            toGlobalAction = actToFstAction::reverseGet
        )

        assertEquals(state.fst, fstStore.stateHolder.value.count)

        /******************* FstStore *******************/
        fstStore.send(FstAction())
        // fst store must update own state
        assertEquals(Fst(1), fstStore.stateHolder.value)
        // and propagate changes to the parent state
        assertEquals(state.copy(fst = 1), store.stateHolder.value)

        // and vise versa scoped store must receive updates from the parent state
        store.send(Act.IncFst)
        assertEquals(state.copy(fst = 2), store.stateHolder.value)
        assertEquals(Fst(2), fstStore.stateHolder.value)

        /******************* SndStore *******************/
        val sndStore = store.scope<Snd, SndAction>(
            toLocalValue = intStateToSnd::get,
            toGlobalAction = actToSndAction::reverseGet
        )

        assertEquals(state.snd, sndStore.stateHolder.value.count)

        sndStore.send(SndAction())
        // fst store must update own state
        assertEquals(Snd(1), sndStore.stateHolder.value)
        // and propagate changes to the parent state
        assertEquals(state.copy(fst = 2, snd = 1), store.stateHolder.value)

        // and vise versa scoped store must receive updates from the parent state
        store.send(Act.IncSnd)
        assertEquals(state.copy(fst = 2, snd = 2), store.stateHolder.value)
        assertEquals(Snd(2), sndStore.stateHolder.value)

        /******************* TrdStore *******************/
        val trdStore = store.scope<Trd, TrdAction>(
            toLocalValue = intStateToTrd::get,
            toGlobalAction = actToTrdAction::reverseGet
        )

        assertEquals(state.trd, trdStore.stateHolder.value.count)

        trdStore.send(TrdAction())
        // fst store must update own state
        assertEquals(Trd(1), trdStore.stateHolder.value)
        // and propagate changes to the parent state
        assertEquals(state.copy(fst = 2, snd = 2, trd = 1), store.stateHolder.value)

        // and vise versa scoped store must receive updates from the parent state
        store.send(Act.IncTrd)
        assertEquals(state.copy(fst = 2, snd = 2, trd = 2), store.stateHolder.value)
        assertEquals(Trd(2), trdStore.stateHolder.value)

    }

    @Test
    fun store_SequentialDelayedUpdates() = runBlocking {
        val state = IntState(0, 0, 0)
        val latch = CountDownLatch(1)
        val longRunningEffect = flow<Act> {
            delay(1000)
            println("longRunningEffect, thread = ${threadName()}")
            emit(Act.IncFst)
            latch.countDown()
        }
            .eraseToEffect()
            .runOn(Dispatchers.IO)
        val reducer = Reducer<IntState, Act, Unit> { intState, action, _ ->
            if (action == Act.Inc) {
                reduced(intState.copy(fst = intState.fst + 1), longRunningEffect)
            } else {
                reduced(intState.copy(fst = intState.fst + 1))
            }
        }

        val store = Store.init(
            initialState = state,
            reducer = reducer,
            environment = Unit,
            storeDispatcher = Dispatchers.Unconfined
        )

        assertEquals(state, store.stateHolder.value)

        val eventsCount = 10

        store.send(Act.Inc)
        repeat(eventsCount) {
            store.send(Act.IncFst)
        }
        latch.await()
        assertEquals(state.copy(fst = 12), store.stateHolder.value)
    }

    @Test
    fun store_DisposedShouldNotReceiveUpdates() {
        val state = IntState(0, 0, 0)
        val reducer = Reducer<IntState, Act, Unit> { intState, act, _ ->
            when (act) {
                Act.Inc -> reduced(
                    value = intState.copy(
                        fst = intState.fst + 1,
                        snd = intState.snd + 1,
                        trd = intState.trd + 1
                    ),
                    effect = Effect.none()
                )
                else -> reduced(intState)
            }
        }
        val store = Store.init(
            initialState = state,
            reducer = reducer,
            environment = Unit,
            storeDispatcher = Dispatchers.Unconfined
        )

        assertEquals(state, store.stateHolder.value)

        store.release()

        store.send(Act.IncFst)
        assertEquals(state, store.stateHolder.value)
    }

    @Test
    fun store_ReleaseDisposesScope() {
        val state = IntState(0, 0, 0)
        val reducer = mock<Reducer<IntState, Act, Unit>>()
        val store = Store.init(
            initialState = state,
            reducer = reducer,
            environment = Unit
        )

        assertEquals(state, store.stateHolder.value)
        assertTrue(store.storeScope.isActive)

        store.release()

        assertFalse(store.storeScope.isActive)
    }
}