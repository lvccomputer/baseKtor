package android.ncdev.basektornetwork.core.dialog

import android.app.Dialog
import android.graphics.drawable.ColorDrawable
import android.ncdev.basektornetwork.R
import android.ncdev.basektornetwork.core.BaseActivity
import android.os.Bundle
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.DialogFragment

abstract class FullScreenDialog(@LayoutRes contentLayoutId:Int):DialogFragment(contentLayoutId) {
    open var allowBackToCancel:Boolean = true
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        isCancelable = allowBackToCancel
        return super.onCreateDialog(savedInstanceState)
    }
    override fun onStart() {
        super.onStart()
        val window = dialog?.window ?: return
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        window.setBackgroundDrawable(ColorDrawable(requireActivity().getColor(R.color.primaryBackground)))
    }
    fun showLoading() = (requireActivity() as? BaseActivity)?.showLoading()

    fun hideLoading() = (requireActivity() as? BaseActivity)?.hideLoading()

}