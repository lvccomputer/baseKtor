package android.ncdev.basektornetwork.ui.home

import android.ncdev.basektornetwork.core.base.BaseViewModel
import android.ncdev.common.coroutines.Resource
import android.ncdev.common.utils.asLiveData
import android.ncdev.core_db.model.GirlModel
import android.ncdev.girl_photo.network.repository.GirlPhotoRepository
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val girlPhotoRepository: GirlPhotoRepository,
) : BaseViewModel() {
    private val _girlPhotoDownloadLiveData = MutableLiveData<Resource<List<GirlModel>>>()
    val girlPhotoDownloadLiveData = _girlPhotoDownloadLiveData.asLiveData()

    private val _girlPhotoListLiveData = MutableLiveData<List<GirlModel>>()
    val girlPhotoListLiveData = _girlPhotoListLiveData.asLiveData()
    var lastId: Long = -1
    var isLoading = false
    var limit = 60

    init {
        getGirl()
    }

    private fun getGirl() = viewModelScope.launch {

        if (girlPhotoRepository.getAll().isNotEmpty()) {
            Log.e(TAG, "getGirl: " + girlPhotoRepository.getAll().isNotEmpty())
            loadData()
            return@launch
        }
        _girlPhotoDownloadLiveData.postValue(Resource.Loading)

        runCatching {
            girlPhotoRepository.getGirls()
        }.onSuccess {
            var id = -1
            val girlModels = it.map { GirlModel(id++, it) }
            _girlPhotoDownloadLiveData.postValue(Resource.OnLoadingFinished)
            girlPhotoRepository.addGirls(girlModels)
        }.onFailure {
            _girlPhotoDownloadLiveData.postValue(Resource.OnLoadingFinished)
            showError(it)
        }
    }

    fun loadData() = viewModelScope.launch {
        _girlPhotoDownloadLiveData.postValue(Resource.Loading)
        runCatching {
            girlPhotoRepository.loadMore(-1, limit)
        }.onSuccess {
            Log.e(TAG, "loadData: " + it.size + " ${it.last().id}")
//            if (it.isNotEmpty()) {
//                lastId = it.last().id.toLong()
//            }
            limit += it.size
            isLoading = false
            _girlPhotoDownloadLiveData.postValue(Resource.Success(it))
        }.onFailure {
            _girlPhotoDownloadLiveData.postValue(Resource.Success(emptyList()))
        }

    }

    companion object {
        private const val TAG = "HomeViewModel"
    }
}