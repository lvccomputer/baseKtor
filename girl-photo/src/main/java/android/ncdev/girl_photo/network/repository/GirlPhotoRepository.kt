package android.ncdev.girl_photo.network.repository

import android.ncdev.girl_photo.network.services.GirlService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GirlPhotoRepository @Inject constructor(private val service: GirlService) {

    suspend fun getGirls() = withContext(Dispatchers.IO) { service.getGirls() }
}