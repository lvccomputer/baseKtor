package android.ncdev.basektornetwork.ui.dashboard

import android.ncdev.common.flow.inBackground
import android.ncdev.common.flow.share
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlin.properties.Delegates

class DashboardViewModel : ViewModel() {

    var rawNumber by Delegates.observable("0") { property, oldValue, newValue ->
        if (oldValue != newValue) {
            _dotNumberFormatFlow.value = newValue
        }
    }
    private val _dotNumberFormatFlow = MutableStateFlow("0")
    val dotNumberFormatFlow =
        _dotNumberFormatFlow.asSharedFlow().inBackground().share(scope = viewModelScope)
}