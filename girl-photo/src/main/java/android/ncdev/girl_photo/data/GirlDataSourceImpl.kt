package android.ncdev.girl_photo.data

import android.ncdev.core_db.model.GirlModel
import android.ncdev.core_db.repository.GirlDbRepository
import kotlinx.coroutines.flow.Flow

class GirlDataSourceImpl(private val girlDbRepository: GirlDbRepository) : GirlDataSource {
    override val girlsFlow: Flow<List<GirlModel>> = girlDbRepository.getAllAsFlow()

    override fun addGirls(list: List<GirlModel>) {
        girlDbRepository.addGirls(list)
    }

    override fun addGirl(girlModel: GirlModel) {
        girlDbRepository.addGirl(girlModel)
    }

    override suspend fun loadMore(lastId: Long,limit:Int): List<GirlModel> {
       return girlDbRepository.loadMore(lastId,limit)
    }

    override fun getAll(): List<GirlModel> {
        return girlDbRepository.getAll()
    }
}