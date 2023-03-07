package android.ncdev.girl_photo.network.repository

import android.ncdev.core_db.model.GirlModel
import android.ncdev.girl_photo.data.GirlDataSource
import android.ncdev.girl_photo.network.services.GirlService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GirlPhotoRepository @Inject constructor(
    private val service: GirlService,
    private val girlDataSource: GirlDataSource
) {

    val girlFlows = girlDataSource.girlsFlow
    fun addGirls(list: List<GirlModel>) {
        girlDataSource.addGirls(list)
    }

    fun addGirl(girlModel: GirlModel) {
        girlDataSource.addGirl(girlModel)
    }
    suspend fun getAll() = girlDataSource.getAll()

    suspend fun loadMore(lastId: Long ,limit : Int) = girlDataSource.loadMore(lastId,limit)

    suspend fun getGirls() = withContext(Dispatchers.IO) { service.getGirls() }
}