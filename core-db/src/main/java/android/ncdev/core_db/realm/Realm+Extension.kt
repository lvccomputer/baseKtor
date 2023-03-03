package android.ncdev.core_db.realm

import io.realm.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

fun <E : RealmModel> Realm.nextID(clazz: Class<E>): Int {
    val max = where(clazz).max("id")
    return 1 + (max?.toInt() ?: 0)
}


fun <E : RealmObject> E.persist() {
    Realm.getDefaultInstance().use {
        it.executeTransaction {
            it.insertOrUpdate(this)
        }
    }
}

fun <E : RealmObject> List<E>.persist() {
    Realm.getDefaultInstance().use {
        it.executeTransaction {
            it.insertOrUpdate(this)
        }
    }
}

/**
 * Delete all entries of this type in database
 */
fun <T : RealmModel> T.deleteAll(onDeleteSuccess: () -> Unit) {
    Realm.getDefaultInstance().use {
        it.executeTransaction {
            it.where(this.javaClass).findAll().deleteAllFromRealm()
            onDeleteSuccess.invoke()
        }
    }
}

fun <T : RealmModel> deleteAll(clazz: Class<T>, onDeleteSuccess: () -> Unit) {
    Realm.getDefaultInstance().use {
        it.executeTransaction {
            it.where(clazz).findAll().deleteAllFromRealm()
            onDeleteSuccess.invoke()
        }
    }
}


fun <E : RealmObject> E.update(block: E.() -> Unit) {
//    realm?.use {
//        it.executeTransaction {
//            block()
//        }
//    } ?: run {
//        block()
//    }
    realm?.executeTransaction {
        block()
    } ?: run {
        block()
    }
}

fun <E : RealmObject> E.delete() {
    realm?.use {
        it.executeTransaction {
            this.deleteFromRealm()
        }
    }
}

fun <T : RealmObject> T.detached(): T {
    return realm?.copyFromRealm(this) ?: this
}

fun <T : RealmObject> List<T>.detached(): List<T> {
    return map { it.realm?.copyFromRealm(it) ?: it }
}

fun <T : RealmObject> Flow<List<T>>.detached(): Flow<List<T>> {
    return map { it.first().realm?.copyFromRealm(it) ?: it }
}

fun singleTransaction(block: (Realm) -> Unit) {
    Realm.getDefaultInstance().use {
        it.executeTransaction { realm ->
            block(realm)
        }
    }
}

fun <E> RealmQuery<E>.notIn(fieldName: String, values: Collection<Long?>): RealmQuery<E> {
    if (values.size > 1) beginGroup()
    for (item in values) {
        notEqualTo(fieldName, item)
    }
    if (values.size > 1) endGroup()
    return this
}
