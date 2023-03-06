package android.ncdev.basektornetwork.core.base

import android.ncdev.basektornetwork.R
import android.ncdev.basektornetwork.getAppContext
import android.ncdev.common.coroutines.Event
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
typealias TitleAndMessage = Pair<String, String?>

open class BaseViewModel : ViewModel() {
    private val _errorLiveData = MutableLiveData<Event<String>>()
    val errorLiveData: LiveData<Event<String>> = _errorLiveData

    private val _errorWithTitleLiveData = MutableLiveData<Event<TitleAndMessage>>()
    val errorWithTitleLiveData: LiveData<Event<TitleAndMessage>> = _errorWithTitleLiveData

    private val _messageLiveData = MutableLiveData<Event<String>>()
    val messageLiveData: LiveData<Event<String>> = _messageLiveData

    fun showMessage(text: String) {
        _messageLiveData.postValue(Event(text))
    }

    fun showError(title: String, text: String) {
        _errorWithTitleLiveData.value = Event(title to text)
    }

    fun showError(text: String = getAppContext().getString(R.string.connection_error)) {
        _errorLiveData.postValue(Event(text))
    }

    fun showError(throwable: Throwable) {
        throwable.printStackTrace()
        throwable.message?.let(this::showError)
    }
}