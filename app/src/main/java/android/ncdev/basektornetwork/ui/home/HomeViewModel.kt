package android.ncdev.basektornetwork.ui.home

import android.ncdev.basektornetwork.core.base.BaseViewModel
import android.ncdev.common.coroutines.Resource
import android.ncdev.common.flow.inBackground
import android.ncdev.common.flow.share
import android.ncdev.common.utils.asLiveData
import android.ncdev.common.utils.extensions.jsonToList
import android.ncdev.common.utils.extensions.toJsonString
import android.ncdev.core_db.model.GirlModel
import android.ncdev.girl_photo.model.GirlModelUI
import android.ncdev.girl_photo.network.repository.GirlPhotoRepository
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val girlPhotoRepository: GirlPhotoRepository,
) : BaseViewModel() {
    private val _girlPhotoDownloadLiveData = MutableLiveData<Resource<List<GirlModelUI>>>()
    val girlPhotoDownloadLiveData = _girlPhotoDownloadLiveData.asLiveData()

    private val _girlPhotoListFlow = MutableSharedFlow<List<GirlModelUI>>()
    val girlPhotoListFlow =
        _girlPhotoListFlow.asSharedFlow().inBackground().share(scope = viewModelScope)

    private var idSelected = -2//default is not selected

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
        runCatching {
            Log.e(TAG, "loadData: "+limit)
            girlPhotoRepository.loadMore(-2, limit)
        }.onSuccess {
            _girlPhotoListFlow.emit(it.map {
                GirlModelUI(
                    id = it.id,
                    url = it.url,
                    isSelected = idSelected == it.id
                )
            })
        }.onFailure {
            showError(it)
        }
    }

    fun setSelected(id: Int) = viewModelScope.launch {
        idSelected = id
        val listPhotoAsJson = girlPhotoListFlow.first().toJsonString()
        val listPhotoCloned = listPhotoAsJson.jsonToList<GirlModelUI>().map {
            it.isSelected = it.id == id
            it
        }
        _girlPhotoListFlow.emit(listPhotoCloned)
    }

    companion object {
        private const val TAG = "HomeViewModel"
    }
}