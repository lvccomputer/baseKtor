package android.ncdev.basektornetwork.ui.notifications

import android.ncdev.basektornetwork.core.base.BaseViewModel
import android.ncdev.common.flow.inBackground
import android.ncdev.common.flow.share
import android.ncdev.core_db.model.SampleModel
import android.ncdev.core_db.repository.SampleRepository
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NotificationsViewModel @Inject constructor(
    private val sampleRepository: SampleRepository
) : BaseViewModel() {


    val sampleModelListFlow = sampleRepository.getAllAsFlow()
        .inBackground().share(scope = viewModelScope)


    fun insertSampleModel(sampleModel: SampleModel) {
        sampleRepository.addSample(sampleModel)
    }

}