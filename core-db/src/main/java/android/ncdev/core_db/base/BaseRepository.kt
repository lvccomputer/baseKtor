package android.ncdev.core_db.base

import android.ncdev.core_db.realm.detached
import io.realm.Realm
import io.realm.RealmChangeListener
import io.realm.RealmObject
import io.realm.RealmQuery
import io.realm.RealmResults
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import java.lang.reflect.ParameterizedType

abstract class BaseRepository<T : RealmObject> {
    private val realmClass: Class<T> =
        (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<T>

    val realm: Realm get() = Realm.getDefaultInstance()
    val realmQuery: RealmQuery<T> get() = realm.where(realmClass)
    private fun <E> realmExecute(block: (Realm) -> E): E {
        return realm.use {
            block(it)
        }
    }


    fun <E> realmExecuteTransaction(block: (Realm) -> E) {
        realm.use {
            it.executeTransaction {
                block(it)
            }
        }
    }

    open suspend fun insertOrUpdate(vararg obj: T) {
        insertOrUpdate(obj.asList())
    }

    open suspend fun insertOrUpdate(list: List<T>) {
        return realmExecuteTransaction {
            it.insertOrUpdate(list)
        }
    }

    fun delete(block: (RealmQuery<T>) -> RealmResults<T>) {
        return realmExecuteTransaction {
            block(it.where(realmClass)).deleteAllFromRealm()
        }
    }

    fun queryObject(block: (RealmQuery<T>) -> T?): T? {
        return realmExecute {
            block(it.where(realmClass))?.detached()
        }
    }

    fun queryList(block: (RealmQuery<T>) -> List<T>): List<T> {
        return realmExecute {
            block(it.where(realmClass)).detached()
        }
    }

    fun findAll(): List<T> {
        return realmExecute {
            it.where(realmClass).findAll().detached()
        }
    }

    fun findAllFlow(): Flow<List<T>> = handlerAndReturnListFlow {
        realm.where(realmClass)
    }.flowOn(Dispatchers.IO)

    @OptIn(ExperimentalCoroutinesApi::class)
    fun handlerAndReturnListFlow(block: () -> RealmQuery<T>): Flow<List<T>> =
        callbackFlow {

            val results = block().findAllAsync()!!
            val realm = results.realm!!

            val listener = RealmChangeListener<RealmResults<T>> { t ->
                this.trySend(t.detached()).isSuccess
            }

            results.addChangeListener(listener)
            this.trySend(results.detached()).isSuccess

            awaitClose {
                if (!realm.isClosed) {
                    results.removeChangeListener(listener)
                    realm.close()
                }
            }
        }.flowOn(Dispatchers.Main)
}