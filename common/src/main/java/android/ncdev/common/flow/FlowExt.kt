package android.ncdev.common.flow

import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*

inline fun <T, R> Flow<List<T>>.mapList(crossinline mapper: suspend (T) -> R) = map { list ->
    list.map { item -> mapper(item) }
}

/**
 * Modifies flow so that it firstly emits [LoadingState.Loading] state.
 * Then emits each element from upstream wrapped into [LoadingState.Loaded] state.
 */
fun <T> Flow<T>.withLoading(): Flow<LoadingState<T>> {
    return map<T, LoadingState<T>> { LoadingState.Loaded(it) }
        .onStart { emit(LoadingState.Loading()) }
}

suspend fun <T> Flow<LoadingState<T>>.firstOnLoad(): T = transform {
    collect {
        if (it is LoadingState.Loaded<T>) {
            emit(it.data)
        }
    }
}.first()

fun <T1, T2> combineToPair(flow1: Flow<T1>, flow2: Flow<T2>): Flow<Pair<T1, T2>> =
    combine(flow1, flow2, ::Pair)

/**
 * Modifies flow so that it firstly emits [LoadingState.Loading] state for each element from upstream.
 * Then, it constructs new source via [sourceSupplier] and emits all of its items wrapped into [LoadingState.Loaded] state
 * Old suppliers are discarded as per [Flow.transformLatest] behavior
 */
fun <T, R> Flow<T>.withLoading(sourceSupplier: suspend (T) -> Flow<R>): Flow<LoadingState<R>> {
    return transformLatest { item ->
        emit(LoadingState.Loading<R>())

        val newSource = sourceSupplier(item).map { LoadingState.Loaded(it) }

        emitAll(newSource)
    }
}

/**
 * Similar to [Flow.takeWhile] but emits last element too
 */
fun <T> Flow<T>.takeWhileInclusive(predicate: suspend (T) -> Boolean) = transformWhile {
    emit(it)

    predicate(it)
}

inline fun <T, R> Flow<T?>.mapNullable(crossinline mapper: suspend (T) -> R): Flow<R?> {
    return map { it?.let { mapper(it) } }
}

/**
 * Modifies flow so that it firstly emits [LoadingState.Loading] state for each element from upstream.
 * Then, it constructs new source via [sourceSupplier] and emits all of its items wrapped into [LoadingState.Loaded] state
 * Old suppliers are discarded as per [Flow.transformLatest] behavior
 */
fun <T, R> Flow<T>.withLoadingSingle(sourceSupplier: suspend (T) -> R): Flow<LoadingState<R>> {
    return transformLatest { item ->
        emit(LoadingState.Loading<R>())

        val newSource = LoadingState.Loaded(sourceSupplier(item))

        emit(newSource)
    }
}

fun <T> Flow<T>.asLiveData(scope: CoroutineScope): LiveData<T> {
    val liveData = MutableLiveData<T>()

    onEach {
        liveData.value = it
    }.launchIn(scope)

    return liveData
}

data class ListDiff<T>(
    val removed: List<T>,
    val addedOrModified: List<T>,
    val all: List<T>
)

fun <T> Flow<List<T>>.diffed(): Flow<ListDiff<T>> {
    return zipWithPrevious().map { (previous, new) ->
        val addedOrModified = new - previous.orEmpty().toSet()
        val removed = previous.orEmpty() - new.toSet()

        ListDiff(removed = removed, addedOrModified = addedOrModified, all = new)
    }
}

fun <T> Flow<T>.zipWithPrevious(): Flow<Pair<T?, T>> = flow {
    var current: T? = null

    collect {
        emit(current to it)

        current = it
    }
}

fun <T> singleReplaySharedFlow() =
    MutableSharedFlow<T>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

fun <T> Flow<T>.inBackground() = flowOn(Dispatchers.Default)
fun <T> Flow<T>.share(
    started: SharingStarted = SharingStarted.Eagerly,
    scope: CoroutineScope
) = shareIn(scope, started = started, replay = 1)

fun <T> Flow<T>.shareInBackground(
    started: SharingStarted = SharingStarted.Eagerly,
    scope: CoroutineScope
) = inBackground().share(started,scope)


inline fun <R> MutableStateFlow<Boolean>.withFlagSet(action: () -> R): R {
    value = true

    val result = action()

    value = false

    return result
}

fun <T> Flow<T>.observe(
    scope: LifecycleCoroutineScope,
    collector: FlowCollector<T>,
) {
    scope.launchWhenResumed {
        collect(collector)
    }
}

fun MutableStateFlow<Boolean>.toggle() {
    value = !value
}

fun <T> flowOf(producer: suspend () -> T) = flow {
    emit(producer())
}

inline fun <T> flowOfAll(crossinline producer: suspend () -> Flow<T>): Flow<T> = flow {
    emitAll(producer())
}

fun <T> List<Flow<T>>.accumulate(): Flow<List<T>> {
    return accumulate(*this.toTypedArray())
}

fun <T> accumulate(vararg flows: Flow<T>): Flow<List<T>> {
    val flowsList = flows.mapIndexed { index, flow -> flow.map { index to flow } }
    val resultOfFlows = MutableList<T?>(flowsList.size) { null }
    return flowsList
        .merge()
        .map {
            resultOfFlows[it.first] = it.second.first()
            resultOfFlows.filterNotNull()
        }
}

fun <T> accumulateFlatten(vararg flows: Flow<List<T>>): Flow<List<T>> {
    return accumulate(*flows).map { it.flatten() }
}

fun <A, B, R> unite(flowA: Flow<A>, flowB: Flow<B>, transform: (A?, B?) -> R): Flow<R> {
    var aResult: A? = null
    var bResult: B? = null

    return merge(
        flowA.onEach { aResult = it },
        flowB.onEach { bResult = it },
    ).map { transform(aResult, bResult) }
}

fun <A, B, C, R> unite(
    flowA: Flow<A>,
    flowB: Flow<B>,
    flowC: Flow<C>,
    transform: (A?, B?, C?) -> R
): Flow<R> {
    var aResult: A? = null
    var bResult: B? = null
    var cResult: C? = null

    return merge(
        flowA.onEach { aResult = it },
        flowB.onEach { bResult = it },
        flowC.onEach { cResult = it },
    ).map { transform(aResult, bResult, cResult) }
}

fun <T> firstNonEmpty(
    vararg sources: Flow<List<T>>
): Flow<List<T>> = accumulate(*sources)
    .transform { collected ->
        val isAllLoaded = collected.size == sources.size
        val flattenResult: List<T> = collected.flatten()

        if (isAllLoaded || flattenResult.isNotEmpty()) {
            emit(flattenResult)
        }
    }

fun <T> Flow<T>.observeInLifecycle(
    lifecycleCoroutineScope: LifecycleCoroutineScope,
    observer: FlowCollector<T>,
) {
    lifecycleCoroutineScope.launchWhenResumed {
        collect(observer)
    }
}

fun <T> Map<out T, MutableStateFlow<Boolean>>.checkEnabled(key: T) = get(key)?.value ?: false

suspend inline fun <reified T> Flow<T?>.firstNotNull(): T = first { it != null } as T