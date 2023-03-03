package android.ncdev.basektornetwork.view.toolbar

import android.content.Context
import android.ncdev.basektornetwork.R
import android.ncdev.basektornetwork.databinding.ViewToolbarBottomsheetBinding
import android.ncdev.common.utils.viewbinding.viewBinding
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.use

class ToolbarViewBottomSheet @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {

    private val binding = viewBinding(ViewToolbarBottomsheetBinding::inflate)

    init {
        context.obtainStyledAttributes(attrs, R.styleable.ToolbarViewBottomSheet).use {
            binding.tvTitle.text = it.getText(R.styleable.ToolbarViewBottomSheet_title) ?: ""

        }
    }
    val tvTitle get() = binding.tvTitle
    val btnBack get() = binding.btnBack
}