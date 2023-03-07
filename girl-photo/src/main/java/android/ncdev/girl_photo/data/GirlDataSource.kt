package android.ncdev.girl_photo.data

import android.ncdev.core_db.model.GirlModel
import kotlinx.coroutines.flow.Flow

interface GirlDataSource {

    val girlsFlow: Flow<List<GirlModel>>

    fun addGirls(list: List<GirlModel>)
    fun addGirl(girlModel: GirlModel)
    suspend fun loadMore(lastId: Long,limit:Int): List<GirlModel>
    fun getAll(): List<GirlModel>
}