package android.ncdev.core_db.repository

import android.ncdev.core_db.base.BaseRepository
import android.ncdev.core_db.model.GirlModel
import android.util.Log
import io.realm.Sort
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn

class GirlDbRepository : BaseRepository<GirlModel>() {
    fun addGirls(list: List<GirlModel>) {
        realmExecuteTransaction {
            it.insertOrUpdate(list)
        }
    }

    fun addGirl(girlModel: GirlModel) {
        realmExecuteTransaction {
            it.insertOrUpdate(girlModel)
        }
    }

    fun getAll() = queryList {
        it.findAll()
    }

    fun loadMore(index: Long, limit: Int) = queryList {
        Log.e("TAG", "loadMore: "+limit.toLong() )
        it.greaterThan("id", index).limit(limit.toLong()).sort("id", Sort.ASCENDING)
            .findAll()
    }

    fun getAllAsFlow() = handlerAndReturnListFlow {
        realm.where(GirlModel::class.java)
    }.flowOn(Dispatchers.Main)

}