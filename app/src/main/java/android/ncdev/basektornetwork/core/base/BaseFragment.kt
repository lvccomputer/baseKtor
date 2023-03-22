package android.ncdev.basektornetwork.core.base

import android.ncdev.basektornetwork.R
import android.ncdev.basektornetwork.core.toolbar.ToolbarConfiguration
import android.ncdev.basektornetwork.utils.navigateSafe
import android.ncdev.basektornetwork.utils.showToast
import android.ncdev.basektornetwork.view.toolbar.SlidingTopErrorView
import android.ncdev.basektornetwork.view.toolbar.ToolbarView
import android.ncdev.common.coroutines.Event
import android.ncdev.common.coroutines.EventObserver
import android.ncdev.common.coroutines.Resource
import android.ncdev.common.utils.hideKeyboard
import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.flow.Flow

abstract class BaseFragment(@LayoutRes private val layoutResId: Int) : Fragment(layoutResId) {

    open val toolbarConfiguration: ToolbarConfiguration? = null

    open val hideBottomBar: Boolean = false

    open val isSendTracking: Boolean = true
    abstract val binding : ViewBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar(toolbarConfiguration)
        setupBottomBar()
        initView()
        observeViewModels()
    }

    val toolbar: ToolbarView? get() = requireActivity().findViewById(R.id.toolbarView)

    open fun setupToolbar(toolbarConfiguration: ToolbarConfiguration?) {
        toolbar?.isVisible = toolbarConfiguration != null
        toolbar?.configure(toolbarConfiguration ?: return)
    }

    abstract fun initView()
    abstract fun observeViewModels()
    open fun observeError(viewModel: BaseViewModel){
        viewModel.errorLiveData.observeEvent {
            it.showToast(requireContext())
        }
    }

    private val bottomBar: BottomNavigationView? get() = requireActivity().findViewById(R.id.bottomBar)

    private fun setupBottomBar() {
        bottomBar?.isGone = hideBottomBar
    }

    fun nav(directions: NavDirections) {
        findNavController().navigateSafe(directions)
    }

    open fun navBack() = run {
        view?.hideKeyboard()
        findNavController().navigateUp()
    }

    fun showLoading() = (requireActivity() as? BaseActivity)?.showLoading()

    fun hideLoading() = (requireActivity() as? BaseActivity)?.hideLoading()

    private val slidingTopErrorView: SlidingTopErrorView? get() = requireActivity().findViewById(R.id.slidingTopErrorView)

    fun showHeadUpError(errorMessage: CharSequence?, title: String? = null) {
        val safeTitle = title ?: getString(R.string.error)
        val safeErrorMessage = errorMessage ?: getString(R.string.unknown_error)
        slidingTopErrorView?.addErrorMessage(safeTitle, safeErrorMessage)
    }

    fun <V> LiveData<Event<V>>.observeEvent(observer: (V) -> Unit) {
        observe(viewLifecycleOwner, EventObserver(observer::invoke))
    }

    fun <V> Flow<V>.observe(collector: suspend (V) -> Unit) {
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            collect(collector)
        }
    }

    fun <V> LiveData<Resource<V>>.observeResource(
        onSuccess: (V) -> Unit,
        onFailed: ((Throwable) -> Unit)? = null
    ) {
        observe(viewLifecycleOwner) {
            it.use(
                onLoading = {
                    showLoading()
                },
                onLoadingFinished = {
                    hideLoading()
                },
                onSuccess = {
                    onSuccess.invoke(it)
                },
                onFailed = {
                    onFailed?.invoke(it.error)
                }
            )
        }
    }
}
