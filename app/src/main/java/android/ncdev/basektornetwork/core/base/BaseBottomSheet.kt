package android.ncdev.basektornetwork.core.base

import android.ncdev.basektornetwork.R
import android.ncdev.common.coroutines.Event
import android.ncdev.common.coroutines.EventObserver
import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.LayoutRes
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.flow.Flow

abstract class BaseBottomSheet(
    @LayoutRes private val layoutResId: Int
) : BottomSheetDialogFragment() {

    open val isFullPage = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Use custom theme for fragment
        //Change background for popup menu
        val contextThemeWrapper = ContextThemeWrapper(activity, R.style.AppTheme)
        return inflater.cloneInContext(contextThemeWrapper).inflate(layoutResId, container, false)
    }

    override fun getTheme(): Int {
        return R.style.Custom_BottomSheetDialog
    }


    override fun onStart() {
        super.onStart()
        val bottomSheet = getBottomSheetFrameLayout()
        if (bottomSheet != null) {
            with(BottomSheetBehavior.from(bottomSheet)) {
                skipCollapsed = true
                state = BottomSheetBehavior.STATE_EXPANDED
            }
            if (isFullPage) {
                makeBottomSheetFullPage(bottomSheet)
            }
        }
    }


    private fun getBottomSheetFrameLayout(): FrameLayout? {
        return dialog?.findViewById(com.google.android.material.R.id.design_bottom_sheet) as? FrameLayout?
    }

    private fun makeBottomSheetFullPage(bottomSheetLayout: FrameLayout) {
        val fullExceptedHeight =
            activity?.resources?.displayMetrics?.heightPixels?.times(0.92)
        if (fullExceptedHeight != null) {
            if (bottomSheetLayout.layoutParams.height != fullExceptedHeight.toInt()) {
                bottomSheetLayout.layoutParams.height = fullExceptedHeight.toInt()
                bottomSheetLayout.requestLayout()
            }
        }
    }

    fun navBack() = findNavController().navigateUp()
    fun showLoading() = (requireActivity() as? BaseActivity)?.showLoading()
    fun hideLoading() = (requireActivity() as? BaseActivity)?.hideLoading()

    fun <V> LiveData<Event<V>>.observeEvent(scope: LifecycleOwner, observer: (V) -> Unit) {
        observe(scope, EventObserver(observer::invoke))
    }
    fun <V> Flow<V>.observe(scope: LifecycleOwner, collector: suspend (V) -> Unit) {
        scope.lifecycleScope.launchWhenResumed {
            collect(collector)
        }
    }
}