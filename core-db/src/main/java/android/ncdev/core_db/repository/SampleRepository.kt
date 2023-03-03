package android.ncdev.core_db.repository

import android.ncdev.core_db.base.BaseRepository
import android.ncdev.core_db.model.SampleModel
import android.ncdev.core_db.realm.nextID

class SampleRepository : BaseRepository<SampleModel>() {

    fun addSample(sampleModel: SampleModel) {
        realmExecuteTransaction {
            sampleModel.apply {//increase id
                id = it.nextID(sampleModel.javaClass).toLong()
            }
            it.insertOrUpdate(sampleModel)
        }
    }

    /**
     * use for available list id
     */
    fun addSamples(sampleModels: List<SampleModel>) {
        realmExecuteTransaction {
            it.insertOrUpdate(sampleModels)
        }
    }

    suspend fun updateSample(sampleModel: SampleModel) {
        super.insertOrUpdate(sampleModel)
    }

    suspend fun updateSample(sampleModels: List<SampleModel>) {
        super.insertOrUpdate(sampleModels)
    }

    override suspend fun insertOrUpdate(vararg obj: SampleModel) {
        error("Call updateSample")
    }

    suspend fun deleteChainLocal(sampleModel: SampleModel) {
        delete {
            it.equalTo("id", sampleModel.id).findAll()
        }
    }

    suspend fun deleteChainLocal(list: List<SampleModel>) {
        list.forEach {
            deleteChainLocal(it)
        }
    }

    suspend fun deleteAll() {
        delete {
            it.findAll()
        }
    }

    fun getAll() = queryList { it.findAll() }

    fun getAllAsFlow() = findAllFlow()

}