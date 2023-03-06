package android.ncdev.basektornetwork.core.base

import android.ncdev.basektornetwork.R
import android.ncdev.basektornetwork.core.dialog.FullScreenDialog
import android.ncdev.common.utils.showWithStateCheck
import androidx.fragment.app.FragmentManager

class LoadingDialogFragment : FullScreenDialog(R.layout.dialog_loading) {

    override var allowBackToCancel = false

    companion object {

        fun show(
            childFragmentManager: FragmentManager,
        ): LoadingDialogFragment {
            return LoadingDialogFragment().apply {
            }.also {
                it.showWithStateCheck(childFragmentManager)
            }
        }
    }
}
